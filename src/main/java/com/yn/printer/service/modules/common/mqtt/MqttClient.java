package com.yn.printer.service.modules.common.mqtt;

import com.yn.printer.service.config.CommonConfig;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * mqtt 客户端
 *
 * @author : Jonas Chan
 * @since : 2024/1/3 16:02
 */
@Slf4j
@Configuration
public class MqttClient {

    @Autowired
    MqttConfig mqttConfig;

    @Autowired
    MqttReceiver mqttReceiver;

    @Bean
    public MessageChannel inputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel outputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MqttConnectOptions mqttConnectOptions() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setUserName(mqttConfig.username);
        mqttConnectOptions.setPassword(mqttConfig.password.toCharArray());
        mqttConnectOptions.setServerURIs(new String[]{mqttConfig.host});
        mqttConnectOptions.setKeepAliveInterval(mqttConfig.heartbeat);
        return mqttConnectOptions;
    }

    @Bean
    public MqttPahoClientFactory factory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(mqttConnectOptions());
        return factory;
    }

    @Bean
    public MessageProducer inputClient() {
        log.info(" MQTT 入站通道初始化 ");
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                mqttConfig.clientId + "::" + CommonConfig.instance, factory(), mqttConfig.topicReceiveDevice + "#");
        adapter.setCompletionTimeout(mqttConfig.completionTimeout);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(mqttConfig.topicReceiveQos);
        adapter.setOutputChannel(inputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "outputChannel")
    public MessageHandler outputClient() {
        log.info(" MQTT 出站通道初始化 ");
        MqttPahoMessageHandler adapter = new MqttPahoMessageHandler(
                mqttConfig.clientId + "::" + new Date().getTime(), factory());
        adapter.setAsync(true);
        adapter.setDefaultQos(mqttConfig.topicSendQos);
        adapter.setDefaultTopic(mqttConfig.topicSendDevice);
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "inputChannel")
    public MessageHandler handler() {
        return message -> {
            Object topicObject = message.getHeaders().get("mqtt_receivedTopic");
            if (topicObject != null) {
                String topic = (String) topicObject;
                String data = message.getPayload().toString();
//                log.info(topic + " : " + data);
                mqttReceiver.receive(topic, data);
            }
        };
    }

}
