package com.yn.printer.service.modules.member.controller;

import com.yn.printer.service.modules.member.dto.WxAppletLoginDTO;
import com.yn.printer.service.modules.member.service.IMemberService;
import com.yn.printer.service.modules.member.vo.MemberLoginVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author : Jonas Chan
 * @since : 2023/12/13 15:40
 */
@Validated
@Api(value = "MemberController", tags = "公共端-会员")
@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private IMemberService memberService;

    @ApiOperation(value = "会员-微信授权登录")
    @PostMapping(value = "/login/wxAuth")
    public MemberLoginVo loginByWxAuth(@RequestBody WxAppletLoginDTO dto) {
        return memberService.loginByWxAuth(dto);
    }

    @ApiOperation(value = "会员-短信验证码登录")
    @GetMapping(value = "/login/sms")
    public MemberLoginVo loginBySms(@RequestParam String phoneNumber, @RequestParam String smsCode) {
        return memberService.loginBySms(phoneNumber, smsCode);
    }
    @ApiOperation(value = "会员-简易登录")
    @GetMapping(value = "/login/sim")
    public MemberLoginVo login(@RequestParam String phoneNumber) {
        return memberService.login(phoneNumber);
    }

    @ApiOperation(value = "会员-密码登录")
    @GetMapping(value = "/login/word")
    public MemberLoginVo loginByPassWord(@RequestParam String phoneNumber,@RequestParam String password) {
        return memberService.loginByPassword(phoneNumber,password);
    }

    @ApiOperation(value = "会员-验证码发送")
    @GetMapping(value = "/login/send")
    public Boolean sendVerificationCode(@RequestParam String phoneNumber,@RequestParam String email) {
        return memberService.sendVerificationCode(phoneNumber,email);
    }

    @ApiOperation(value = "会员-重置密码")
    @GetMapping(value = "/login/rest")
    public MemberLoginVo restPassword(@RequestParam String phoneNumber,@RequestParam String verificationCode) {
        return memberService.restPassword(phoneNumber,verificationCode);
    }



}
