package com.yn.printer.service.modules.dataAnalysis.unitl;

import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

public class CustomTemporalAdjusters {
    //构建一个.TemporalAdjuster，用于调整日期为季度的开始。
    public static TemporalAdjuster firstDayOfQuarter() {
        return temporal -> {
            int month = temporal.get(ChronoField.MONTH_OF_YEAR);
            int quarter = (month - 1) / 3 + 1;
            int firstMonthOfQuarter = (quarter - 1) * 3 + 1;
            return temporal.with(ChronoField.MONTH_OF_YEAR, firstMonthOfQuarter)
                    .with(TemporalAdjusters.firstDayOfMonth());
        };
    }

}