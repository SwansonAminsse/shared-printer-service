package com.yn.printer.service.modules.common.mqtt.dto;

import com.yn.printer.service.modules.common.mqtt.MessageType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author : Jonas Chan
 * @since : 2024/1/3 17:09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "PrintOrderPush", description = "打印订单推送实例")
public class PrintOrderPush extends BasePush {

  @ApiModelProperty(value = "命令")
  final MessageType cmd = MessageType.ORDER_INFO;

  // 订单号
  private String code;

  // 打印任务
  private List<PrintTaskPush> printTaskPushList;

}
