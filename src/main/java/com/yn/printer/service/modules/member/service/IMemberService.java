package com.yn.printer.service.modules.member.service;

import com.yn.printer.service.modules.member.dto.MemberModifyInfoDto;
import com.yn.printer.service.modules.member.dto.WxAppletLoginDTO;
import com.yn.printer.service.modules.member.entity.Member;
import com.yn.printer.service.modules.member.vo.MemberLoginVo;
import com.yn.printer.service.modules.member.vo.integralBalanceVO;

import java.math.BigDecimal;

public interface IMemberService {

    /**
     * 微信小程序授权登录
     *
     * @param dto
     * @return
     */
    MemberLoginVo loginByWxAuth(WxAppletLoginDTO dto);

    /**
     * 短信验证码登录
     *
     * @param phoneNumber
     * @param smsCode
     * @return
     */
    MemberLoginVo loginBySms(String phoneNumber, String smsCode);

    MemberLoginVo login(String phoneNumber);

    /**
     * 修改个人信息
     *
     * @param dto
     * @return
     */

    Boolean modifyInfo(MemberModifyInfoDto dto);
    MemberLoginVo createLoginVo(Member member);
    integralBalanceVO getIntegralBalance();


    MemberLoginVo loginByPassword(String phoneNumber, String password);

    MemberLoginVo restPassword(String phoneNumber, String verificationCode);

    MemberLoginVo fix(String password);


    MemberLoginVo bindEmail(String email, String verificationCode);

    boolean sendVerificationCode(String phoneNumber, String email);

    Boolean authentication(String tel, String trueName, String idenNo);

    BigDecimal getChargeBalance();

}
