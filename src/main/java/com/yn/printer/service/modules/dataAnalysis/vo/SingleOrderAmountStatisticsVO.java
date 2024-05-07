package com.yn.printer.service.modules.dataAnalysis.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SingleOrderAmountStatisticsVO {
    @ApiModelProperty(value = "1元以下订单数量", example = "0")
    private long oneYuanLess;
    @ApiModelProperty(value = "1-2元订单数量", example = "0")
    private long oneToTwoYuan;
    @ApiModelProperty(value = "2-4元订单数量", example = "0")
    private long twoToFourYuan;
    @ApiModelProperty(value = "4-10元订单数量", example = "0")
    private long fourToTenYuan;
    @ApiModelProperty(value = "10-20元订单数量", example = "0")
    private long tenToTwentyYuan;
    @ApiModelProperty(value = "20元以上订单数量", example = "0")
    private long twentyYuanMore;
}
