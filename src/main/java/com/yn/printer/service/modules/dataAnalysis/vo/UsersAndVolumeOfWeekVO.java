package com.yn.printer.service.modules.dataAnalysis.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UsersAndVolumeOfWeekVO {
    @ApiModelProperty(value = "交易用户数")
    private List<Long> userDealNumber;
    @ApiModelProperty(value = "交易订单额")
    private List<BigDecimal> ordersTurnoverNumber;
}