package com.yn.printer.service.modules.auth.controller;

import com.yn.printer.service.modules.auth.dto.LoginDTO;
import com.yn.printer.service.modules.auth.service.UserService;
import com.yn.printer.service.modules.auth.vo.LoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 用户控制层
 *
 * @author huabiao
 * @create 2023/12/11  10:25
 **/
@Validated
@Api(value = "UserController", tags = "公共端-用户")
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @ApiOperation(value = "用户-登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public LoginVO login(@RequestBody @Valid LoginDTO loginDTO) {
        return userService.login(loginDTO);
    }
}
