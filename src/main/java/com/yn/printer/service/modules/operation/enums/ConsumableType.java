package com.yn.printer.service.modules.operation.enums;

import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public enum ConsumableType {

    C1("耗材A");

    private final String name;

    public String getName() {
        return name;
    }

}