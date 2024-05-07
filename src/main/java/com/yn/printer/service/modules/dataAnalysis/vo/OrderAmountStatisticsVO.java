package com.yn.printer.service.modules.dataAnalysis.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderAmountStatisticsVO {
    @ApiModelProperty(value = "订单打印类型")
    private List<String> orderPrintType;
    @ApiModelProperty(value = "文档打印金额", example = "0.00")
    private BigDecimal documentPrintAmount;
    @ApiModelProperty(value = "照片打印金额", example = "0.00")
    private BigDecimal photoPrintAmount;
    @ApiModelProperty(value = "文档照片打印金额", example = "0.00")
    private BigDecimal documentAndPhotoPrintAmount;
}
