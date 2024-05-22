package com.yn.printer.service.modules.dataAnalysis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "ChannelOrderVO", description = "依据渠道和时间统计订单数据")
public class ChannelOrderVO {
    @ApiModelProperty(value = "订单类型统计")
    private OrderStatisticsVO orderStatistics;
    @ApiModelProperty(value = "类型金额统计")
    private OrderAmountStatisticsVO orderAmountStatistics;
    @ApiModelProperty(value = "单笔订单金额统计")
    private   SingleOrderAmountStatisticsVO singleOrderAmountStatistics;
    @ApiModelProperty(value = "各类收入占比")
    private OrderIncomeRateVo orderIncomeRate;
    @ApiModelProperty(value = "站点排行")
    private List<DeviceRankVO> deviceRankList;

}