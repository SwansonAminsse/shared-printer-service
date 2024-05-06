package com.yn.printer.service.modules.settlement.vo;

import com.yn.printer.service.modules.meta.enums.PaperType;
import com.yn.printer.service.modules.operation.enums.ConsumableType;
import com.yn.printer.service.modules.operation.enums.OperationsType;
import com.yn.printer.service.modules.operation.enums.ProcessingStatus;
import com.yn.printer.service.modules.operation.vo.DevicesListVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import static com.yn.printer.service.modules.operation.util.DistanceCalculator.calculateDistance;

@Data
@ApiModel(value = "IncomeSumVO", description = "收益信息")
public class IncomeVO {

    @ApiModelProperty(value = "设备名")
    private String deviceName;
    @ApiModelProperty(value = "渠道商名")
    private String channelName;
    @ApiModelProperty(value = "收入")
    private BigDecimal deviceIncome;;
    @ApiModelProperty(value = "结算比例")
    private Integer settlementRatio;
    @ApiModelProperty(value = "结算金额")
    private BigDecimal settlementAmount;
    @ApiModelProperty(value = "已收款金额")
    private BigDecimal alreadyPaid;;
    @ApiModelProperty(value = "场租")
    private BigDecimal rentalFees;;
    @ApiModelProperty(value = "纸张成本")
    private BigDecimal paperCost = BigDecimal.ZERO;
    @ApiModelProperty(value = "耗材成本")
    private BigDecimal consumableCost = BigDecimal.ZERO;
    @ApiModelProperty(value = "毛利润")
    private BigDecimal grossProfit = BigDecimal.ZERO;
    @ApiModelProperty(value = "毛利率")
    private Double grossMargin = 0.00;

public  IncomeVO(String deviceName, String channelName, BigDecimal deviceIncome, Integer settlementRatio,BigDecimal settlementAmount ,BigDecimal alreadyPaid, BigDecimal rentalFees) {
    this.deviceName = deviceName;
    this.channelName = channelName;
    this.deviceIncome = deviceIncome;
    this.settlementRatio = settlementRatio;
    this.alreadyPaid = alreadyPaid;
    this.rentalFees = rentalFees;
}

    public IncomeVO cost() {
        BigDecimal rentalFees = this.getRentalFees();
        BigDecimal paperCost = this.getPaperCost();
        BigDecimal consumableCost = this.getConsumableCost();
        BigDecimal grossProfit = this.getDeviceIncome().subtract(rentalFees).subtract(paperCost).subtract(consumableCost);
        BigDecimal grossMargin;
        if (grossProfit.compareTo(BigDecimal.ZERO) == 0) {
            grossMargin = BigDecimal.ONE.multiply(new BigDecimal(100));
        } else {
            grossMargin = grossProfit.divide(grossProfit, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
        }
        this.setGrossMargin((double) grossMargin.intValue());
        this.setGrossProfit(grossProfit);
        return this;
    }
}