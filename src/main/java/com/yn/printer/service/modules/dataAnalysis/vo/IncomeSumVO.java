package com.yn.printer.service.modules.dataAnalysis.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class IncomeSumVO {
    // 收入
    @ApiModelProperty(value = "设备收入", example = "0.00")
    private BigDecimal deviceIncome = BigDecimal.ZERO;

    // 佣金分成金额
    @ApiModelProperty(value = "佣金分成金额", example = "0.00")
    private BigDecimal settlementAmount = BigDecimal.ZERO;

    // 场租
    @ApiModelProperty(value = "场租费用", example = "0.00")
    private BigDecimal rentalFees = BigDecimal.ZERO;

    // 纸张成本
    @ApiModelProperty(value = "纸张成本", example = "0.00")
    private BigDecimal paperCost = BigDecimal.ZERO;

    // 耗材成本
    @ApiModelProperty(value = "耗材成本", example = "0.00")
    private BigDecimal consumableCost = BigDecimal.ZERO;

    // 毛利润
    @ApiModelProperty(value = "毛利润", example = "0.00")
    private BigDecimal grossProfit = BigDecimal.ZERO;


    @ApiModelProperty(value = "毛利率", example = "0.00")
    private Double grossMargin = 0.0;

    // 增长率
    @ApiModelProperty(value = "增长率", example = "0.00")
    private Double addMargin = 0.0;

    // 店租成本
    @ApiModelProperty(value = "店租成本", example = "0.00")
    private BigDecimal storeCost = BigDecimal.ZERO;

    // 运维成本
    @ApiModelProperty(value = "运维成本", example = "0.00")
    private BigDecimal taskCost = BigDecimal.ZERO;

    @ApiModelProperty(value = "设备数量", example = "0")
    private long devicesNumber = 0;


}