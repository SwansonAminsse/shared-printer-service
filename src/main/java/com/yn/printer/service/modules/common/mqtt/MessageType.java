package com.yn.printer.service.modules.common.mqtt;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MessageType {

    HEARTBEAT("HEARTBEAT", "心跳"),
    LOGIN("LOGIN", "登录"),
    LOGOUT("LOGOUT", "登出"),
    ORDER_INFO("ORDER_INFO", "订单信息"),
    PRINT("PRINT", "打印"),
    PRINT_STATUS("PRINT_STATUS", "打印状态"),
    CHANGE("CHANGE", "变更");

    private final String value;
    private final String name;

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

}
