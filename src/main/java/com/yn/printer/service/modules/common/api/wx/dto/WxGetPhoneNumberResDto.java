package com.yn.printer.service.modules.common.api.wx.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 微信小程序获取手机号返回体
 * <p>
 * 详情参见: https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/user-login/code2Session.html
 *
 * @author : Jonas Chan
 * @since : 2023/12/13 11:44
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WxGetPhoneNumberResDto extends WxResDto {
    PhoneInfo phoneInfo; // 用户手机号信息
}
