package com.yn.printer.service.modules.settlement.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
@Data
@ApiModel(value = "UserTotalVO", description = "用户分析")
public class UserTotalVO {

    @ApiModelProperty(value = "设备名")
    private String deviceName;
    @ApiModelProperty(value = "渠道商名")
    private String channelName;
    @ApiModelProperty(value = "详细地址")
    private String address;
    @ApiModelProperty(value = "交易用户数")
    private Integer userNumber ;
    @ApiModelProperty(value = "新增用户")
    private Integer addUser;
    @ApiModelProperty(value = "用户增长率")
    private Double growthRate;
    @ApiModelProperty(value = "订单数")
    private Integer ordersNumber;
    @ApiModelProperty(value = "复购率")
    private Double repurchaseRate;
    @ApiModelProperty(value = "收入金额")
    private BigDecimal countOrderAmount;

}