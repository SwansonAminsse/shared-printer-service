package com.yn.printer.service.modules.common.mqtt.dto;

import com.yn.printer.service.modules.common.mqtt.MessageType;
import lombok.Data;

@Data
public class BasePush {

    MessageType cmd;

}
