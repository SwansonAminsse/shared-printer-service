package com.yn.printer.service.modules.common.api.wx.dto;

import lombok.Data;

/**
 * 手机号信息
 *
 * @author : Jonas Chan
 * @since : 2023/12/13 18:00
 */
@Data
public class PhoneInfo {
    String phoneNumber; // 用户绑定的手机号（国外手机号会有区号）
}
