package com.yn.printer.service.modules.common.api.wx.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 微信小程序获取AccessToken返回体
 * <p>
 * 详情参见: https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/user-login/code2Session.html
 *
 * @author : Jonas Chan
 * @since : 2023/12/13 11:44
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WxGetAccessTokenResDto extends WxResDto {
    String access_token; // 获取到的凭证
    Integer expires_in; // 凭证有效时间，单位：秒。目前是7200秒之内的值
}
