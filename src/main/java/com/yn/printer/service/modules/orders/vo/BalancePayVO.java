package com.yn.printer.service.modules.orders.vo;

import com.yn.printer.service.modules.common.api.wx.dto.JsPayInfo;
import com.yn.printer.service.modules.orders.enums.PayStatus;
import io.swagger.annotations.ApiModelProperty;

public class BalancePayVO {
    @ApiModelProperty(value = "用户余额")
    String qrCode;

    @ApiModelProperty(value = "支付结果")
    PayStatus payStatus;



}