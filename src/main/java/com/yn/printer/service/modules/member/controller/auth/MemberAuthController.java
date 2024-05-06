package com.yn.printer.service.modules.member.controller.auth;

import com.yn.printer.service.modules.member.dto.MemberModifyInfoDto;
import com.yn.printer.service.modules.member.service.IMemberService;
import com.yn.printer.service.modules.member.service.IPointsFileService;
import com.yn.printer.service.modules.member.vo.MemberLoginVo;
import com.yn.printer.service.modules.member.vo.integralBalanceVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * @author : Jonas Chan
 * @since : 2023/12/14 17:06
 */
@Validated
@Api(value = "MemberAuthController", tags = "会员端-会员")
@RestController
@RequestMapping("/member/auth")
public class MemberAuthController {

    @Autowired
    private IMemberService memberService;
    @Autowired
    private IPointsFileService printerService;

    @ApiOperation(value = "会员-修改个人信息")
    @PostMapping(value = "/modifyInfo")
    public Boolean loginByWxAuth(@RequestBody MemberModifyInfoDto dto) {
        return memberService.modifyInfo(dto);
    }
    @ApiOperation(value = "会员-查询积分、余额")
    @GetMapping(value = "/getIntegral")
    public integralBalanceVO getIntegral() {
        return memberService.getIntegralBalance();
    }

//    @ApiOperation(value = "添加积分")
//    @GetMapping(value = "/creatAddPointsFile")
//    public void creatAddPointsFile(@RequestParam BigDecimal amount, @RequestParam double Magnification) {
//        printerService.creatAddPointsFile(amount);
//    }
    @ApiOperation(value = "会员-修改密码")
    @PostMapping(value = "/fix")
    public MemberLoginVo fixPassWord(@RequestBody String password) {
        return memberService.fix(password);
    }

    @ApiOperation(value = "会员-绑定邮箱")
    @GetMapping(value = "/bindEmail")
    public MemberLoginVo bindEmail(@RequestParam String email,@RequestParam String verificationCode) {
        return memberService.bindEmail(email,verificationCode);
    }


}
