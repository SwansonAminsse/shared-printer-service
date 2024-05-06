package com.yn.printer.service.modules.settlement.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "IncomeSumVO", description = "收益信息")
public class IncomeSumVO {

    @ApiModelProperty(value = "总收入")
    private BigDecimal totalIncome = BigDecimal.ZERO;

    @ApiModelProperty(value = "平均结算比例")
    private Double averageSettlementRatio = 0.0;

    @ApiModelProperty(value = "总结算余额")
    private BigDecimal sumSettlementAmount = BigDecimal.ZERO;


    public IncomeSumVO(BigDecimal totalIncome, Double averageSettlementRatio, BigDecimal sumSettlementAmount) {
        this.totalIncome = totalIncome;
        this.averageSettlementRatio = averageSettlementRatio;
        this.sumSettlementAmount = sumSettlementAmount;
    }

}