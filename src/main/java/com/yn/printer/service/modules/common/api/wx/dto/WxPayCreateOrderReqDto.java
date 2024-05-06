package com.yn.printer.service.modules.common.api.wx.dto;

import lombok.Data;

/**
 * 微信支付请求实例
 *
 * @author : Jonas Chan
 * @since : 2023/12/13 11:44
 */
@Data
public class WxPayCreateOrderReqDto {
    String outTradeNo; // 本地订单号
    String description; // 订单说明
    Integer amount; // 订单金额
}
