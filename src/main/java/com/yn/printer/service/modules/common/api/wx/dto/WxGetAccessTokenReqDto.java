package com.yn.printer.service.modules.common.api.wx.dto;

import lombok.Data;

/**
 * 微信小程序获取AccessToken请求体
 * <p>
 * 详情参见: https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/user-login/code2Session.html
 *
 * @author : Jonas Chan
 * @since : 2023/12/13 11:44
 */
@Data
public class WxGetAccessTokenReqDto {
    String grant_type = "client_credential"; // 填写 client_credential
    String appid; // 小程序唯一凭证，即 AppID，可在「微信公众平台 - 设置 - 开发设置」页中获得。（需要已经成为开发者，且账号没有异常状态）
    String secret; // 小程序唯一凭证密钥，即 AppSecret，获取方式同 appid
}
