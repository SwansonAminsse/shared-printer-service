package com.yn.printer.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 通用 配置
 *
 * @author : Jonas Chan
 * @since : 2024/1/3 16:03
 */
@Configuration
public class CommonConfig {

    public static String instance;

    @Value("${common.instance}")
    public void setInstance(String instance) {
        CommonConfig.instance = instance;
    }
}
