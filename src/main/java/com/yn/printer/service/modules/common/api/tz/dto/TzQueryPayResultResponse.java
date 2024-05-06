package com.yn.printer.service.modules.common.api.tz.dto;

import lombok.Data;

/**
 * 天章支付-查询支付结果-请求实例
 *
 * @author : Jonas Chan
 * @since : 2023/12/25 9:44
 */
@Data
public class TzQueryPayResultResponse {

    // MPS系统订单编号
    String orderId;

    // 支付金额
    String amount;

    // 支付状态，20：未支付，21：支付成功，22：支付失败，23：支付中
    String tradeState;

}
