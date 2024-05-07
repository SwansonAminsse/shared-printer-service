package com.yn.printer.service.modules.operation.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DeviceType {
    SNJ("室内机"),
    SWJ("室外机");
    private String name;

    public String getName() {
        return name;
    }
}
