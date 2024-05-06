package com.yn.printer.service.modules.auth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 登录DTO
 *
 * @author huabiao
 * @create 2023/12/11  10:25
 **/
@Data
@ApiModel(value = "LoginDTO", description = "登录DTO")
public class LoginDTO implements Serializable {

    @ApiModelProperty(value = "账号", required = true)
    @NotBlank
    private String account;

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank
    private String password;
}
