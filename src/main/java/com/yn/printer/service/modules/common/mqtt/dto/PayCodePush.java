package com.yn.printer.service.modules.common.mqtt.dto;

import com.yn.printer.service.modules.common.mqtt.MessageType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author : Jonas Chan
 * @since : 2024/1/3 17:09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "MemberLoginPush", description = "订单支付码推送实例")
public class PayCodePush extends BasePush {

  @ApiModelProperty(value = "命令")
  final MessageType cmd = MessageType.ORDER_INFO;

  @ApiModelProperty(value = "支付码地址")
  private String payCodeUrl;

}
