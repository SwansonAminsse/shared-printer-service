package com.yn.printer.service.modules.common.api.tz.dto;

import lombok.Data;

/**
 * 天章支付-创建支付二维码-返回实例
 *
 * @author : Jonas Chan
 * @since : 2023/12/25 9:44
 */
@Data
public class TzCreateQrCodeResponse {

    // 支付交易单号
    String trade_no;

    // 支付方式，WECHAT：微信二维码，ALIPAY：支付宝二维码
    String pay_type;

    // MPS系统支付流水号
    String mer_trace;

    // 支付成功回调地址，如果为空则不进行回调
    String notify_url;

    // 支付系统支付流水号
    String payment_detail_no;

    // 支付地址，需MPS系统根据该地址自动生成二维码
    String qr_link;

    // 二维码失效时间戳
    String qr_expire_time;

    // 支付金额
    String amount;

}
