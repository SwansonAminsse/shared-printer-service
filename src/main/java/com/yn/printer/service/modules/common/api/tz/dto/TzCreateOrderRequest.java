package com.yn.printer.service.modules.common.api.tz.dto;

import lombok.Data;

/**
 * 天章支付-创建订单-请求实例
 *
 * @author : Jonas Chan
 * @since : 2023/12/25 9:44
 */
@Data
public class TzCreateOrderRequest {

    // MPS系统订单编号
    String orderId;

    // 交易的当前日期格式YYYYMMDD
    String merDate;

    // 交易金额
    String amount;

    // 交易渠道：MPS系统请传1
    String orderChannel = "1";

    // 订单备注
    String remark;

}
