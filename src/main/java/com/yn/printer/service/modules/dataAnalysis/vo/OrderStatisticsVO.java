package com.yn.printer.service.modules.dataAnalysis.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class OrderStatisticsVO {
    @ApiModelProperty(value = "打印数量", example = "0")
    private long printNumber;
    @ApiModelProperty(value = "打印类型", example = "照片")
    private String orderPrintType;
}
