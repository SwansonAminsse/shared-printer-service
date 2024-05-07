package com.yn.printer.service.modules.dataAnalysis.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class IncometotalVO {
    @ApiModelProperty(value = "今日收入", example = "0.00")
    private BigDecimal deviceIncome = BigDecimal.ZERO;

    @ApiModelProperty(value = "同比增长率", example = "0.00")
    private Double yesterdayRate = 0.00;
    @ApiModelProperty(value = "同期增长率", example = "0.00")
    private Double lastWeekRate = 0.00;
}
