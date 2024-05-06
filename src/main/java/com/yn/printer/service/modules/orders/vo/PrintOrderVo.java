package com.yn.printer.service.modules.orders.vo;

import com.yn.printer.service.modules.orders.enums.PayStatus;
import com.yn.printer.service.modules.orders.enums.TransactionStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.util.List;

/**
 * 打印记录
 *
 * @author : Jonas Chan
 * @since : 2024/1/4 15:22
 */
@Data
@ApiModel(value = "PrintOrderVo", description = "打印记录")
public class PrintOrderVo {

    @ApiModelProperty(value = "交易状态 INIT(\"初始化\"),\n" +
            "  IN_PROGRESS(\"进行中\"),\n" +
            "  COMPLETE(\"已完成\"),\n" +
            "  CANCELED(\"已取消\")")
    private TransactionStatus transactionStatus;

    @ApiModelProperty(value = "支付状态 PAID(\"已付款\"),\n" +
            "  UN_PAID(\"未付款\"),\n" +
            "  REFUNDED(\"已退款\")")
    private PayStatus payStatus;

    @ApiModelProperty(value = "下单时间")
    String createTime;

    @ApiModelProperty(value = "终端商户名称")
    String shopName;

    @ApiModelProperty(value = "设备名称")
    String deviceName;

    @ApiModelProperty(value = "任务列表")
    List<PrintTaskVo> printTaskVoList;

    @ApiModelProperty(value = "合计金额")
    BigDecimal amount;

}
