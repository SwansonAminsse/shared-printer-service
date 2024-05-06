package com.yn.printer.service.modules.member.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author : Jonas Chan
 * @since : 2023/12/13 17:41
 */
@Data
@ApiModel(value = "WxAppletLoginDTO", description = "小程序登录数据")
public class WxAppletLoginDTO implements Serializable {

    @NotNull
    @ApiModelProperty(value = "登录码", required = true)
    private String code;

    @ApiModelProperty(value = "获取手机号码", required = true)
    private String getPhoneCode;

//    @NotNull
//    @ApiModelProperty(value = "加密数据", required = true)
//    private String encryptedData;
//
//    @NotNull
//    @ApiModelProperty(value = "加密算法的初始向量", required = true)
//    private String iv;
//
//    @NotNull
//    @ApiModelProperty(value = "weixin session_key", required = true)
//    private String sessionKey;

}
