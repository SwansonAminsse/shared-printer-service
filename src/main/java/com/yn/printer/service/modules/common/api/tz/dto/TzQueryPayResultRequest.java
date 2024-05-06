package com.yn.printer.service.modules.common.api.tz.dto;

import lombok.Data;

/**
 * 天章支付-查询支付结果-请求实例
 *
 * @author : Jonas Chan
 * @since : 2023/12/25 9:44
 */
@Data
public class TzQueryPayResultRequest {

    // MPS系统订单编号
    String orderId;

    // 交易的当前日期格式YYYYMMDD
    String merDate;

}
