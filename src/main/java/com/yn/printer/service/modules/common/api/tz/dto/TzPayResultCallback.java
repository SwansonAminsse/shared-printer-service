package com.yn.printer.service.modules.common.api.tz.dto;

import lombok.Data;

/**
 * 天章支付-支付回调-回调实例
 *
 * @author : Jonas Chan
 * @since : 2023/12/25 9:44
 */
@Data
public class TzPayResultCallback {

    // MPS系统订单编号
    String orderId;

    // 支付交易单号
    String tradeNo;

    // 订单日期，平台在创建订单时日期
    String merDate;

    // 支付方式，WECHAT：微信二维码，ALIPAY：支付宝二维码（暂不支持）
    String payType;

    // 支付金额
    String amount;

}
