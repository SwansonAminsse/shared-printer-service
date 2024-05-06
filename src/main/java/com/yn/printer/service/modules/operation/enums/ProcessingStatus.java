package com.yn.printer.service.modules.operation.enums;
import lombok.AllArgsConstructor;
import lombok.Getter;



@AllArgsConstructor
public enum ProcessingStatus {
    PS1("已处理"),
    PS0("未处理");

    private final String name;
    public String getName() {
        return name;
    }


}