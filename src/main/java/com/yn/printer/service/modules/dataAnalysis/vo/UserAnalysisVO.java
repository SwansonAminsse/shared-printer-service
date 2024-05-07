package com.yn.printer.service.modules.dataAnalysis.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserAnalysisVO {
    @ApiModelProperty(value = "用户总数", example = "0")
    private long userNumber = 0;

    @ApiModelProperty(value = "用户新增数", example = "0")
    private long addUserNumber = 0;

    @ApiModelProperty(value = "老用户数", example = "0")
    private long oldUserNumber = 0;
    @ApiModelProperty(value = "老用户数量比率", example = "0.00")
    private Double oldUserRate = 0.00;
    @ApiModelProperty(value = "新用户数量比率", example = "0.00")
    private Double addUserRate = 0.00;

}
