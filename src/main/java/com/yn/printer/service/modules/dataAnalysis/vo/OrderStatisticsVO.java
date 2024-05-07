package com.yn.printer.service.modules.dataAnalysis.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class OrderStatisticsVO {
    @ApiModelProperty(value = "文档打印数量", example = "0")
    private long documentPrintNumber;
    @ApiModelProperty(value = "照片打印数量", example = "0")
    private long photoPrintNumber;
    @ApiModelProperty(value = "文档照片打印数量", example = "0")
    private long documentAndPhotoPrintNumber;
    @ApiModelProperty(value = "订单打印类型")
    private List<String> orderPrintType;
}
