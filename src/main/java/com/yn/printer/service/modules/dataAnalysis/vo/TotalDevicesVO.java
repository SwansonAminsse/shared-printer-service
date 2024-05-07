package com.yn.printer.service.modules.dataAnalysis.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TotalDevicesVO {
    @ApiModelProperty(value = "设备数量", example = "0")
    private long devicesNumber = 0;
    @ApiModelProperty(value = "总设备收入", example = "0.00")
    private BigDecimal deviceIncome = BigDecimal.ZERO;
    @ApiModelProperty(value = "在线设备数量", example = "0")
    private long onlineDevicesNumber = 0;
    @ApiModelProperty(value = "在线设备数量比率", example = "0.00")
    private Double onlineDevicesRate = 0.00;
    @ApiModelProperty(value = "异常设备数量", example = "0")
    private long abnormalDevicesNumber = 0;
    @ApiModelProperty(value = "异常设备数量比率", example = "0.00")
    private Double abnormalDevicesRate = 0.00;
    @ApiModelProperty(value = "离线设备数量", example = "0")
    private long offlineDevicesNumber = 0;
    @ApiModelProperty(value = "离线设备数量比率", example = "0.00")
    private Double offlineDevicesRate = 0.00;
    @ApiModelProperty(value = "进行设备数量", example = "0")
    private long runDevicesNumber = 0;
    @ApiModelProperty(value = "进行设备数量比率", example = "0.00")
    private Double runDevicesRate = 0.00;
    @ApiModelProperty(value = "用户总数", example = "0")
    private long userNumber = 0;
}
