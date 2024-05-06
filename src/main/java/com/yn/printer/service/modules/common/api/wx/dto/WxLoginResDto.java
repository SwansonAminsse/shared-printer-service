package com.yn.printer.service.modules.common.api.wx.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 微信小程序授权登录返回体
 * <p>
 * 详情参见: https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/user-login/code2Session.html
 *
 * @author : Jonas Chan
 * @since : 2023/12/13 11:44
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WxLoginResDto extends WxResDto{
    String openid; // 用户唯一标识
    String session_key; // 会话密钥
    String unionid; // 用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台账号下会返回，详见 UnionID 机制说明。
    Integer errcode; // 错误码
    String errmsg; // 错误信息
}
