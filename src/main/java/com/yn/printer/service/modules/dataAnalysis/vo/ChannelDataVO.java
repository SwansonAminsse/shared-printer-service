package com.yn.printer.service.modules.dataAnalysis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "ChannelDataVO", description = "依据渠道和时间统计设备、用户和运维数据")
public class ChannelDataVO {
    @ApiModelProperty(value = "渠道设备统计")
    private List<DeviceStatisticsVO> deviceStatistics;
    @ApiModelProperty(value = "用户统计")
    private UserStatisticsVO userStatistics;
    @ApiModelProperty(value = "运维统计")
    private TaskByChannelVO taskByChannel;
}