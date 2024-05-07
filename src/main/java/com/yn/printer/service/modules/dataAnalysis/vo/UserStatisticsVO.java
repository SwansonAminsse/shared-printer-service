package com.yn.printer.service.modules.dataAnalysis.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserStatisticsVO {
    @ApiModelProperty(value = "使用用户", example = "0")
    private long uUserNumber;
    @ApiModelProperty(value = "新用户", example = "0")
    private long newUserNumber;
    @ApiModelProperty(value = "老用户", example = "0")
    private long oldUserNumber;
    @ApiModelProperty(value = "新用户比例", example = "0.00")
    private Double newUserRate;
    @ApiModelProperty(value = "旧用户比例", example = "0.00")
    private Double oldUserRate;

}
