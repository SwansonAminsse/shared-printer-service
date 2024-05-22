package com.yn.printer.service.modules.dataAnalysis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TimeSelect {
    TODAY("今日"),
    THIS_WEEK("本周"),
    THIS_MONTH("本月"),
    THIS_YEAR("年度");

    private final String name;


}