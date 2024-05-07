package com.yn.printer.service.modules.dataAnalysis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "orderVO", description = "订单记录")
public class OrderVO {
    @ApiModelProperty(value = "交易状态 INIT(\"初始化\"),\n" +
            "  IN_PROGRESS(\"进行中\"),\n" +
            "  COMPLETE(\"已完成\"),\n" +
            "  CANCELED(\"已取消\")")
    private String transactionStatus;
    @ApiModelProperty(value = "下单时间")
    private String orderdate;
    @ApiModelProperty(value = "设备名称")
    private String deviceName;
    @ApiModelProperty(value = "下单用户名称")
    private String userName;
    @ApiModelProperty(value = "订单打印类型")
    private String orderPrintType;
    @ApiModelProperty(value = "订单金额")
    private BigDecimal orderAmount;

}
