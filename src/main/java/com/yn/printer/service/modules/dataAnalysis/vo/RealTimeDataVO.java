package com.yn.printer.service.modules.dataAnalysis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@ApiModel(value = "RealTimeDataVO", description = "当前实时数据（不区分渠道商和时间）")
public class RealTimeDataVO {

    @ApiModelProperty(value = "当前实时设备数量（状态），设备总数，和用户总数,收入总数")
    private TotalDevicesVO totalDevices;
    @ApiModelProperty(value = "当前用户的新老用户占比")
    private UserAnalysisVO userAnalysis;
    @ApiModelProperty(value = "当前订单统计和占比")
    private OrderTotalVO orderTotal;
    @ApiModelProperty(value = "实时收入统计和占比")
    private IncometotalVO incomeTotal;


}