package com.yn.printer.service.modules.dataAnalysis.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeviceStatisticsVO {
    @ApiModelProperty(value = "总设备数量", example = "0")
    private long totalDeviceNumber;
    @ApiModelProperty(value = "室外机数量", example = "0")
    private long SNJDeviceNumber;
    @ApiModelProperty(value = "室内机数量", example = "0")
    private long SWJDeviceNumber;
    @ApiModelProperty(value = "设备状态", example = "0")
    private String DeviceState;

}
