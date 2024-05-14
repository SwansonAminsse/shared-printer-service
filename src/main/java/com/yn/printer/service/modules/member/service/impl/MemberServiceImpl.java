package com.yn.printer.service.modules.member.service.impl;

import cn.hutool.core.util.StrUtil;
import com.yn.printer.service.common.consts.CacheConst;
import com.yn.printer.service.common.exception.YnError;
import com.yn.printer.service.common.exception.YnErrorException;
import com.yn.printer.service.interceptor.AuditInterceptor;
import com.yn.printer.service.modules.channel.repository.ChannelPartnerRepository;
import com.yn.printer.service.modules.channel.repository.ChannelUserRepository;
import com.yn.printer.service.modules.common.api.wx.dto.*;
import com.yn.printer.service.modules.common.api.wx.service.WeChatApiService;
import com.yn.printer.service.modules.member.dto.MemberModifyInfoDto;
import com.yn.printer.service.modules.member.dto.WxAppletLoginDTO;
import com.yn.printer.service.modules.member.entity.Member;
import com.yn.printer.service.modules.member.repository.MemberRepository;
import com.yn.printer.service.modules.member.service.IMemberService;
import com.yn.printer.service.modules.member.vo.MemberLoginVo;
import com.yn.printer.service.modules.member.vo.integralBalanceVO;
import com.yn.printer.service.modules.meta.repository.ThirdPartyVouchersRepository;
import com.yn.printer.service.modules.operation.repository.DevicesListRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 会员服务
 *
 * @author : Jonas Chan
 * @since : 2023/12/13 16:42
 */
@Slf4j
@Service
public class MemberServiceImpl implements IMemberService {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    DevicesListRepository devicesListRepository;

    @Autowired
    ChannelPartnerRepository channelPartnerRepository;

    @Autowired
    ChannelUserRepository channelUserRepository;

    @Autowired
    WeChatApiService weChatApiService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    ThirdPartyVouchersRepository thirdPartyVouchersRepository;
    // 验证码长度
    private static final int VERIFICATION_CODE_LENGTH = 6;


    @Override
    public MemberLoginVo loginByWxAuth(WxAppletLoginDTO dto) {
        // 调用微信授权接口获取 openId
        WxLoginReqDto wxLoginReqDto = new WxLoginReqDto();
        wxLoginReqDto.setJs_code(dto.getCode());
        WxLoginResDto wxLoginResDto = weChatApiService.login(wxLoginReqDto);
        if (wxLoginResDto == null || StrUtil.isEmpty(wxLoginResDto.getOpenid()))
            throw new YnErrorException(YnError.YN_300001);

        // 获取接口调用凭证
        WxGetAccessTokenResDto wxGetAccessTokenResDto = weChatApiService.getAccessToken(new WxGetAccessTokenReqDto());
        if (wxGetAccessTokenResDto == null) throw new YnErrorException(YnError.YN_300002);

        // 获取手机号
        WxGetPhoneNumberReqDto wxGetPhoneNumberReqDto = new WxGetPhoneNumberReqDto();
        wxGetPhoneNumberReqDto.setCode(dto.getGetPhoneCode());
        wxGetPhoneNumberReqDto.setOpenid(wxLoginResDto.getOpenid());
        wxGetPhoneNumberReqDto.setAccess_token(wxGetAccessTokenResDto.getAccess_token());
        WxGetPhoneNumberResDto wxGetPhoneNumberResDto = weChatApiService.getPhoneNumber(wxGetPhoneNumberReqDto);
        if (wxGetPhoneNumberResDto == null || wxGetPhoneNumberResDto.getPhoneInfo() == null)
            throw new YnErrorException(YnError.YN_300002);

        // 通过openid获取会员用户
        Member member = memberRepository.findByOpenId(wxLoginResDto.getOpenid());
        if (member == null) {
            // 自动注册会员用户
            member = new Member();
            member.setOpenId(wxLoginResDto.getOpenid());
            member.setJoiningDate(LocalDate.now());
            member.setStatus(true);
            member.setName(wxGetPhoneNumberResDto.getPhoneInfo().getPhoneNumber());
            member.setPhoneNumber(wxGetPhoneNumberResDto.getPhoneInfo().getPhoneNumber());
            memberRepository.save(member);
        }

        return createLoginVo(member);
    }

    @Override
    public MemberLoginVo loginBySms(String phoneNumber, String smsCode) {

        // TODO 验证短信验证码

        // 通过手机号获取会员用户
        Member member = memberRepository.findByPhoneNumber(phoneNumber);
        if (member == null) {
            // 自动注册会员用户
            member = new Member();
            member.setJoiningDate(LocalDate.now());
            member.setStatus(true);
            member.setPhoneNumber(phoneNumber);
            memberRepository.save(member);
        }

        return createLoginVo(member);
    }

    @Override
    public MemberLoginVo login(String phoneNumber) {

        // TODO 验证短信验证码

        // 通过手机号获取会员用户
        Member member = memberRepository.findByPhoneNumber(phoneNumber);
        if (member == null) {
            // 自动注册会员用户
            member = new Member();
            member.setJoiningDate(LocalDate.now());
            member.setStatus(true);
            member.setPhoneNumber(phoneNumber);
            System.out.println("自动注册会员用户");
            memberRepository.save(member);
        }

        return createLoginVo(member);
    }

    @Override
    public Boolean modifyInfo(MemberModifyInfoDto dto) {

        Member member = AuditInterceptor.CURRENT_MEMBER.get();
        if (member == null) throw new YnErrorException(YnError.YN_200001);

        member = memberRepository.findById(member.getId()).orElse(null);
        if (member == null) throw new YnErrorException(YnError.YN_200002);

        BeanUtils.copyProperties(dto, member);
        memberRepository.save(member);

        return true;
    }

    @Override
    public MemberLoginVo createLoginVo(Member member) {
        MemberLoginVo vo = new MemberLoginVo();
        BeanUtils.copyProperties(member, vo);
        // 生成token
        String token = UUID.randomUUID().toString();
        // 添加token数据
        stringRedisTemplate.opsForValue().set(CacheConst.getTokenMember(token), member.getId().toString(), 7, TimeUnit.DAYS);
        stringRedisTemplate.opsForValue().set(CacheConst.getPhoneNumberKey(token), member.getPhoneNumber(), 7, TimeUnit.DAYS);
        vo.setToken(token);
        // 是否运维人员
        vo.setOperation(devicesListRepository.countByDev(member) > 0);
        // 是否运营人员
        vo.setBusiness(channelPartnerRepository.countByAdminPhone(member.getPhoneNumber()) > 0);
        if (!vo.getBusiness()) {
            // 非渠道商管理人员时, 判断是否渠道商内部人员
            vo.setBusiness(channelUserRepository.countByContactPhone(member.getPhoneNumber()) > 0);
        }

        return vo;
    }
    @Override
    public integralBalanceVO getIntegralBalance() {

        Member member = AuditInterceptor.CURRENT_MEMBER.get();
        integralBalanceVO vo = new integralBalanceVO();
        vo.setAccountBalance(member.getAccountBalance());
        vo.setPoints(member.getAccumulatedPoints()-member.getConsumedPoints());
        return vo;
    }

    @Override
    public MemberLoginVo loginByPassword(String phoneNumber, String password) {
        Member member = memberRepository.findFirstByPhoneNumber(phoneNumber);
            if (member == null) throw new YnErrorException(YnError.YN_200001);
        String actualPassword = member.getPassword() != null ? member.getPassword() : "tz123456";
        if (actualPassword.equals(password)) {
            return createLoginVo(member);
        }
        throw new YnErrorException(YnError.YN_200003);
    }
    @Override
    public  MemberLoginVo restPassword(String phoneNumber, String verificationCode) {
        Member member = memberRepository.findByPhoneNumber(phoneNumber);
        if (member == null) throw new YnErrorException(YnError.YN_200001);
        if(!verifyCode(member.getPhoneNumber(), verificationCode))throw new YnErrorException(YnError.YN_200004);
        member.setPassword("tz123456");
        memberRepository.save(member);
        return createLoginVo(member);
    }

    @Override
    public MemberLoginVo fix(String password) {
        Member member = AuditInterceptor.CURRENT_MEMBER.get();
        if (member == null) throw new YnErrorException(YnError.YN_200001);
        member.setPassword(password);
        memberRepository.save(member);
        return createLoginVo(member);
    }

    @Override
    public MemberLoginVo bindEmail(String email, String verificationCode) {
        Member member = AuditInterceptor.CURRENT_MEMBER.get();
        if(verifyCode(member.getPhoneNumber(), verificationCode))throw new YnErrorException(YnError.YN_200004);
        member.setEmail(email);
        memberRepository.save(member);
        return createLoginVo(member);
    }
    @Override
    public boolean sendVerificationCode(String phoneNumber,String email) {
        Member member = memberRepository.findByPhoneNumber(phoneNumber);
        if (member == null) throw new YnErrorException(YnError.YN_200001);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom(thirdPartyVouchersRepository.findAll().get(0).getEmail());
        message.setSubject("验证码");
        message.setText("您的验证码是：" + generateAndSaveVerificationCode(phoneNumber) + "，请妥善保管，勿泄露他人。");
        javaMailSender.send(message);
        return true;
    }

    @Override
    public BigDecimal getChargeBalance() {
        Member member = AuditInterceptor.CURRENT_MEMBER.get();
        return  member.getAccountBalance();
    }


    public String generateAndSaveVerificationCode(String phoneNumber) {
        // 生成随机验证码
        String verificationCode = generateRandomCode();
        // 设置验证码到Redis，有效期为5分钟
        stringRedisTemplate.opsForValue().set("verificationCode:" + phoneNumber, verificationCode, Duration.ofMinutes(5));

        return verificationCode;
    }

    // 生成指定长度的随机数字验证码
    private String generateRandomCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <VERIFICATION_CODE_LENGTH; i++) {
            sb.append((int)(Math.random()*10)); // 生成0-9之间的随机数
        }
        return sb.toString();
    }

    // 校验验证码是否正确（假设从请求参数中获取输入的验证码）
    public boolean verifyCode(String phoneNumber, String userInputCode) {
        String storedCode = stringRedisTemplate.opsForValue().get("verificationCode:" + phoneNumber);
        if (storedCode != null && storedCode.equals(userInputCode)) {
            stringRedisTemplate.delete("verificationCode:" + phoneNumber);
            return true;
        }
        return false;
    }

}
