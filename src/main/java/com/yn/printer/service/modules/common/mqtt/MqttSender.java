package com.yn.printer.service.modules.common.mqtt;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * mqtt 消息发送者
 *
 * @author : Jonas Chan
 * @since : 2024/1/3 16:10
 */
@MessagingGateway
@Component
public interface MqttSender {
    /**
     * MQTT 发送网关
     *
     * @param topic 主题，可以指定不同的数据发布主题，在消息中间件里面体现为不同的消息队列
     * @param data  消息内容
     */
    @Gateway(requestChannel = "outputChannel")
    void send(@Header(MqttHeaders.TOPIC) String topic, String data);
}
