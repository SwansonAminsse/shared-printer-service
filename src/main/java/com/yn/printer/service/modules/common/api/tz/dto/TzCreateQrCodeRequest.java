package com.yn.printer.service.modules.common.api.tz.dto;

import lombok.Data;

/**
 * 天章支付-创建支付二维码-请求实例
 *
 * @author : Jonas Chan
 * @since : 2023/12/25 9:44
 */
@Data
public class TzCreateQrCodeRequest {

    // 支付交易单号
    String trade_no;

    // 支付方式，WECHAT：微信二维码，ALIPAY：支付宝二维码（暂不支持）
    String pay_type = "WECHAT";

    // MPS系统支付流水号
    String mer_trace;

    // 支付成功回调地址，如果为空则不进行回调
    String notify_url = "1";

}
