package com.yn.printer.service.modules.dataAnalysis.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderAmountStatisticsVO {



    @ApiModelProperty(value = "打印类型", example = "照片")
    private String orderPrintType;
    @ApiModelProperty(value = "打印金额", example = "0.00")
    private BigDecimal printAmount;

}
