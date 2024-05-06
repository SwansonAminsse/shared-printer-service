package com.yn.printer.service.modules.auth.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户VO
 *
 * @author huabiao
 * @create 2023/12/11  10:41
 **/
@Data
@ApiModel(value = "UserVO", description = "用户VO")
public class UserVO {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "登录名")
    private String code;

    @ApiModelProperty(value = "昵称")
    private String name;
}
