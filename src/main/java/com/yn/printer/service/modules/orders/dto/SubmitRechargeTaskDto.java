package com.yn.printer.service.modules.orders.dto;

import com.yn.printer.service.modules.orders.enums.PayMode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "SubmitRechargeTaskDto", description = "提交充值任务")
public class SubmitRechargeTaskDto {
    @ApiModelProperty(value = "支付方式 " +
            "  TZ_PAY(\"TZ_PAY\", \"天章支付\"),\n" +
            "  WX_NATIVE_PAY(\"WX_PAY\", \"微信本地支付\"),\n" +
            "  WX_JS_PAY(\"WX_JS_PAY\", \"微信小程序支付\"),\n"
    )
    PayMode payMode;

    @ApiModelProperty(value = "充值金额")
    BigDecimal refillAmount;

}