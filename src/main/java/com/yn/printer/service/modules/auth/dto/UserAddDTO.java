package com.yn.printer.service.modules.auth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户新增DTO
 *
 * @author huabiao
 * @create 2023/12/11  10:55
 **/
@Data
@ApiModel(value = "UserAddDTO", description = "用户新增DTO")
public class UserAddDTO {

    @ApiModelProperty(value = "账号", required = true)
    @NotBlank
    private String account;

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank
    private String password;
}
