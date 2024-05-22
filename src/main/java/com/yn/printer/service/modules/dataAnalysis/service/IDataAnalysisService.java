package com.yn.printer.service.modules.dataAnalysis.service;

import com.yn.printer.service.modules.dataAnalysis.vo.*;

import java.math.BigDecimal;
import java.util.List;

public interface IDataAnalysisService {

    TotalChannelVO getTotalChannel();

    IncomeSumVO totalIncome(Boolean time);

    UserTotalVO getUserTotal(Boolean time);

    DevicesDataVO getDevicesData(Boolean time);

    UsersAndVolumeOfWeekVO getUsersAndVolumeOfWeekVO();

    List<BigDecimal> getGrossProfit();
}
