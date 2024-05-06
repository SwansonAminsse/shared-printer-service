package com.yn.printer.service.modules.dataAnalysis.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserTotalVO {
    @ApiModelProperty(value = "用户总数", example = "0")
    private long userNumber = 0;

    @ApiModelProperty(value = "交易用户数", example = "0")
    private long userDealNumber = 0;

    @ApiModelProperty(value = "用户新增数", example = "0")
    private long addUserNumber = 0;

    @ApiModelProperty(value = "客户增长率", example = "0.00")
    private Double addUserRate = 0.00;

    @ApiModelProperty(value = "订单数", example = "0")
    private long ordersNumber = 0;

    @ApiModelProperty(value = "客户复购率", example = "0.00")
    private Double customerRepurchaseRate = 0.00;

    @ApiModelProperty(value = "交易订单增长率", example = "0.00")
    private Double ordersGrowthRate = 0.00;
}