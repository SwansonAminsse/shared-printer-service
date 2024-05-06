package com.yn.printer.service.modules.common.mqtt;

import lombok.Data;

/**
 * 消息结构
 *
 * @author : Jonas Chan
 * @since : 2024/1/3 16:02
 */
@Data
public class Message {

    private String main;

    private String id;

    private boolean refresh;

}
