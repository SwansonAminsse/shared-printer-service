package com.yn.printer.service.modules.dataAnalysis.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderTotalVO {
    @ApiModelProperty(value = "已完成订单数量", example = "0")
    private long completeOrderNumber = 0;
    @ApiModelProperty(value = "异常订单数量", example = "0")
    private long abnormalOrderNumber = 0;
    @ApiModelProperty(value = "已完成订单总计", example = "0.00")
    private Double completeOrderRate;
    @ApiModelProperty(value = "异常订单总计", example = "0.00")
    private Double abnormalOrderRate;

}
