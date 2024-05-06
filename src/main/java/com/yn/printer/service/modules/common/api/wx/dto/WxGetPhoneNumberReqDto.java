package com.yn.printer.service.modules.common.api.wx.dto;

import lombok.Data;

/**
 * 微信小程序获取手机号请求体
 * <p>
 * 详情参见: https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/user-login/code2Session.html
 *
 * @author : Jonas Chan
 * @since : 2023/12/13 11:44
 */
@Data
public class WxGetPhoneNumberReqDto {
    String access_token; // 接口调用凭证，该参数为 URL 参数，非 Body 参数。使用access_token或者authorizer_access_token
    String code; // 登录时获取的 code
    String openid;
}
