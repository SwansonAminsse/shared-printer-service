package com.yn.printer.service.modules.common.mqtt;

import com.yn.printer.service.common.util.RedisLockUtil;
import com.yn.printer.service.modules.operation.service.IDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * mqtt 消息接收者
 *
 * @author : Jonas Chan
 * @since : 2024/1/3 16:10
 */
@Slf4j
@Component
public class MqttReceiver {

    @Autowired
    MqttConfig mqttConfig;

    @Autowired
    IDeviceService deviceService;

    @Autowired
    RedisLockUtil redisLockUtil;

    void receive(String topic, String data) {
        // 采用分布式锁方式控制每条MQTT消息只被某个负载节点消费一次
        String lockKey = topic + data;
        if (!redisLockUtil.lock(lockKey, "MQTT")) {
            log.debug("当前消息已被其他站点消费 主题:{}, 内容: {}", topic, data);
            return;
        }

        if (topic.startsWith(mqttConfig.topicReceiveDevice)) {
            deviceService.mqttCallback(topic.replace(mqttConfig.topicReceiveDevice, ""), data);
        }

        // Redis锁释放
        if (!redisLockUtil.unlock(lockKey)) log.error("Redis锁释放异常！");
    }

}
