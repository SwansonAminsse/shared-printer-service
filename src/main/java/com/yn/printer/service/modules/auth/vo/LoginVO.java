package com.yn.printer.service.modules.auth.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 登录VO
 *
 * @author huabiao
 * @create 2023/12/11  10:25
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "LoginVO", description = "登录VO")
public class LoginVO {

    @ApiModelProperty(value = "登录令牌")
    private String token;

    @ApiModelProperty(value = "账号")
    private String account;
}
