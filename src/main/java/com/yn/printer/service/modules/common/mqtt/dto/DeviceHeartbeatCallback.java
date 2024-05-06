package com.yn.printer.service.modules.common.mqtt.dto;

import com.yn.printer.service.modules.common.mqtt.MessageType;
import com.yn.printer.service.modules.enums.DeviceStatus;
import com.yn.printer.service.modules.orders.enums.PrintTaskStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author : Jonas Chan
 * @since : 2024/1/3 17:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceHeartbeatCallback extends BaseCallback {

  final MessageType cmd = MessageType.HEARTBEAT;

  // 设备状态
  private DeviceStatus deviceStatus;

  // 异常原因
  private String abnormalReason;

}
