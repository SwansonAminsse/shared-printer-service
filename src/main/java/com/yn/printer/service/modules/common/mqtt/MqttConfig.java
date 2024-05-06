package com.yn.printer.service.modules.common.mqtt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Mqtt 配置
 *
 * @author : Jonas Chan
 * @since : 2024/1/3 16:03
 */
@Configuration
public class MqttConfig {

    @Value("${mqtt.host}")
    public String host;

    @Value("${mqtt.username}")
    public String username;

    @Value("${mqtt.password}")
    public String password;

    @Value("${mqtt.clientId}")
    public String clientId;

    @Value("${mqtt.completionTimeout}")
    public int completionTimeout;

    @Value("${mqtt.heartbeat}")
    public int heartbeat;

    @Value("${mqtt.topicSendDevice}")
    public String topicSendDevice;

    @Value("${mqtt.topicReceiveDevice}")
    public String topicReceiveDevice;

    @Value("${mqtt.topicSendQos}")
    public int topicSendQos;

    @Value("${mqtt.topicReceiveQos}")
    public int topicReceiveQos;

}
