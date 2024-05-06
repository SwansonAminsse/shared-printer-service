package com.yn.printer.service.modules.operation.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TaskStatisticsVO {
    @ApiModelProperty(value = "已读数量")
    private int readedTrueCount;
    @ApiModelProperty(value = "未读数量数量")
    private int readedFalseCount;
    @ApiModelProperty(value = "已处理数量数量")
    private int taskStatusTrueCount;
    @ApiModelProperty(value = "未处理数量数量")
    private int taskStatusFalseCount;
}