package com.yn.printer.service.modules.dataAnalysis.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class DevicesDataVO {
    @ApiModelProperty(value = "设备数量", example = "0")
    private long devicesNumber = 0;

    @ApiModelProperty(value = "新增设备", example = "0")
    private long newDevicesNumber = 0;

    @ApiModelProperty(value = "设备使用次数", example = "0")
    private long useDevicesNumber = 0;

    @ApiModelProperty(value = "设备运维次数", example = "0")
    private long taskDevicesNumber = 0;

    @ApiModelProperty(value = "交易订单额", example = "0.00")
    private BigDecimal ordersTurnoverNumber = BigDecimal.ZERO;

    @ApiModelProperty(value = "单额增长率", example = "0.00")
    private Double turnoverRate = 0.00;

    @ApiModelProperty(value = "广告租金收入", example = "0.00")
    private BigDecimal ordersGrowthRate = BigDecimal.ZERO;

    @ApiModelProperty(value = "广告收入增长率", example = "0.00")
    private Double advertisementRate = 0.00;





}