package com.yn.printer.service.modules.dataAnalysis.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DeviceRankVO {
    @ApiModelProperty(value = "设备收入", example = "0.00")
    private BigDecimal income;
    @ApiModelProperty(value = "设备订单数量", example = "0")
    private long orderNumber;
    @ApiModelProperty(value = "名称")
    private String deviceName;

    public DeviceRankVO(BigDecimal income, long orderNumber, String deviceName) {
        this.income = income;
        this.orderNumber = orderNumber;
        this.deviceName = deviceName;
    }
}
