package com.yn.printer.service.modules.member.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@ApiModel(value = "integralBalanceVO", description = "返回积分、余额")
public class integralBalanceVO {

    @ApiModelProperty(value = "积分")
    private Integer points = 0;

    @ApiModelProperty(value = "余额")
    private BigDecimal accountBalance = BigDecimal.ZERO;
}
