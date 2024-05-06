package com.yn.printer.service.modules.common.api.tz.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 天章支付-创建订单-返回实例
 *
 * @author : Jonas Chan
 * @since : 2023/12/25 9:44
 */
@Data
public class TzCreateOrderResponse {

    // 对应请求中的订单编号
    @NotNull
    String tradeNo;

    // 支付跳转地址，前端可使用二维码生成工具生成二维码
    @NotNull
    String qrCode;

    // 二维码失效时间
    String qrExpireTime;

}
