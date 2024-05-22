package com.yn.printer.service.modules.dataAnalysis.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TaskByChannelVO {
    @ApiModelProperty(value = "总量运维单数量", example = "0")
    private long totalTaskNumber;
    @ApiModelProperty(value = "自动派单数量", example = "0")
    private long autoTaskNumber;
    @ApiModelProperty(value = "人工派单数量", example = "0")
    private long manualTaskNumber;
    @ApiModelProperty(value = "主动运维数量", example = "0")
    private long activeTaskNumber;
    @ApiModelProperty(value = "自动派单完成数量", example = "0")
    private long autoTaskCompleteNumber;
    @ApiModelProperty(value = "人工派单完成数量", example = "0")
    private long manualTaskCompleteNumber;
    @ApiModelProperty(value = "主动运维完成数量", example = "0")
    private long activeTaskCompleteNumber;
    @ApiModelProperty(value = "自动派单比率", example = "0.00")
    private Double autoTaskRate = 0.00;
    @ApiModelProperty(value = "人工派单比率", example = "0.00")
    private Double manualTaskRate = 0.00;
    @ApiModelProperty(value = "主动运维比率", example = "0.00")
    private Double activeTaskRate = 0.00;
}