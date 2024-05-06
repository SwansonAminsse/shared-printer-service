package com.yn.printer.service.modules.settlement.enums;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum Month {
    //本月
    ThisMonth("ThisMonth"),
//上个月
    LastMonth("LastMonth"),

//上上月
    TwoMonthsAgo("TwoMonthsAgo");



    private final String name;

    public String getName() {
        return name;
    }
}
