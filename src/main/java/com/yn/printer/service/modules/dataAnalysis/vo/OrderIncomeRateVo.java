package com.yn.printer.service.modules.dataAnalysis.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderIncomeRateVo {
    @ApiModelProperty(value = "总收入", example = "0.00")
    private BigDecimal totalIncome;
    @ApiModelProperty(value = "总收入订单", example = "0")
    private Long totalIncomeOrder;
    @ApiModelProperty(value = "总收入占比", example = "0.00")
    private Double totalIncomeRate;
    @ApiModelProperty(value = "总收入订单占比", example = "0.00")
    private Double totalIncomeOrderRate;
    @ApiModelProperty(value = "取消收入", example = "0.00")
    private BigDecimal cancelIncome;
    @ApiModelProperty(value = "取消收入订单", example = "0")
    private Long cancelOrder;
    @ApiModelProperty(value = "取消收入占比", example = "0.00")
    private Double cancelIncomeRate;
    @ApiModelProperty(value = "取消收入订单占比", example = "0.00")
    private Double cancelOrderRate;
    @ApiModelProperty(value = "异常收入", example = "0.00")
    private BigDecimal abnormalIncome;
    @ApiModelProperty(value = "异常收入订单", example = "0")
    private Long abnormalOrder;
    @ApiModelProperty(value = "异常收入占比", example = "0.00")
    private Double abnormalIncomeRate;
    @ApiModelProperty(value = "异常收入订单占比", example = "0.00")
    private Double abnormalOrderRate;
    @ApiModelProperty(value = "订单均价", example = "0.00")
    private BigDecimal orderAveragePrice;
}
