package com.yn.printer.service.modules.orders.enums;


import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OrderPrintType {
    DOCUMENT("文档"),
    PHOTO("照片"),
    DOCUMENT_PHOTO("文档照片");
    private final String value;

    public String getValue() {
        return value;
    }
    public static OrderPrintType fromValue(String value) {
        for (OrderPrintType type : values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }
}
