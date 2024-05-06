package com.yn.printer.service.modules.common.mqtt.dto;

import com.yn.printer.service.modules.common.mqtt.MessageType;
import com.yn.printer.service.modules.orders.enums.PrintTaskStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author : Jonas Chan
 * @since : 2024/1/3 17:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PrintTaskCallback extends BaseCallback {

  final MessageType cmd = MessageType.PRINT_STATUS;

  // 任务号
  private String taskCode;

  // 任务状态
  private PrintTaskStatus taskStatus;

  private String interruptReason;

  private Integer complete;

}
