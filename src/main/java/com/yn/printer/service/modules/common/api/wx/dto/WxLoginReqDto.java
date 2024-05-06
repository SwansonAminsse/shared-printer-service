package com.yn.printer.service.modules.common.api.wx.dto;

import lombok.Data;

/**
 * 微信小程序授权登录请求体
 * <p>
 * 详情参见: https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/user-login/code2Session.html
 *
 * @author : Jonas Chan
 * @since : 2023/12/13 11:44
 */
@Data
public class WxLoginReqDto {
    String appid; // 小程序 appId
    String secret; // 小程序 appSecret
    String js_code; // 登录时获取的 code，可通过wx.login获取
    String grant_type = "authorization_code"; // 授权类型，此处只需填写 authorization_code
}
