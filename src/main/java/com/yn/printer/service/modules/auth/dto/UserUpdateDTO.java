package com.yn.printer.service.modules.auth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 用户修改DTO
 *
 * @author huabiao
 * @create 2023/12/11  11:00
 **/
@Data
@ApiModel(value = "UserUpdateDTO", description = "用户修改DTO")
public class UserUpdateDTO {

    @ApiModelProperty(value = "id", required = true)
    @NotNull
    private Long id;

    @ApiModelProperty(value = "用户名称", required = true)
    @NotBlank
    private String name;
}
