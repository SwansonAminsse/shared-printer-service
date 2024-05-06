package com.yn.printer.service.modules.common.api.wx.dto;

import lombok.Data;

/**
 * 微信api返回体 基类
 *
 * @author : Jonas Chan
 * @since : 2023/12/13 18:01
 */
@Data
public class WxResDto {
    Integer errcode; // 错误码
    String errmsg; // 错误信息
}
