package com.yn.printer.service.modules.operation.enums;

import lombok.AllArgsConstructor;
@AllArgsConstructor
public enum Cene {

    School("School"),
    // 学校
    XX("XX"),
    // 写字楼
    XZ("XZ"),
    // 小区
    XQ("XQ"),
    // 办公大厅
    BG("BG"),
    // 商业中心
    SY("SY");

    private final String name;

    public String getName() {
        return name;
    }
}
