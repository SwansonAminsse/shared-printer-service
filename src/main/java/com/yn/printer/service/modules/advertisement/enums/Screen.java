package com.yn.printer.service.modules.advertisement.enums;

import lombok.AllArgsConstructor;

import java.util.Objects;
@AllArgsConstructor
public enum Screen {

    //设备功能屏
    DeviceScreen("DeviceScreen"),

//专用广告屏
    AdvertisingScreen("AdvertisingScreen");

    private final String value;

    public String getValue() {
        return value;
    }
}