package com.yn.printer.service.modules.common.mqtt.dto;

import com.yn.printer.service.modules.common.mqtt.MessageType;
import com.yn.printer.service.modules.orders.enums.PrintTaskStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CodeBack extends BaseCallback {

    final MessageType cmd = MessageType.CHANGE;

    // 版本号
    private String versionNumber;


}