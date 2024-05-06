package com.yn.printer.service.modules.common.mqtt.dto;

import com.yn.printer.service.modules.orders.enums.PayStatus;
import lombok.Data;

/**
 * @author : Jonas Chan
 * @since : 2024/1/3 17:09
 */
@Data
public class OrderPayStatusPush {

  // 订单id
  private Long id;

  // 订单号
  private String code;

  // 支付状态
  private PayStatus payStatus;

}
