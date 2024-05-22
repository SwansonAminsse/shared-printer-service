package com.yn.printer.service.modules.dataAnalysis.service;

import com.yn.printer.service.modules.dataAnalysis.enums.TimeSelect;
import com.yn.printer.service.modules.dataAnalysis.vo.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ICurrentDataAnalysisService {
    TotalDevicesVO totalData();

    Page<OrderVO> getTodayOrders(Pageable pageable);

    UserAnalysisVO getUserTodayAnalysis();

    OrderTotalVO getOrderTotal();

    IncometotalVO getIncomeTotal();

    Page<ChannelPartnerInfo> getAllChannelPartner(Pageable pageable);

    List<DeviceStatisticsVO> getDeviceByChannelPartner(Long channelPartnerId, TimeSelect dateTime);

    UserStatisticsVO getUserByChannelPartnerAndDateTime(Long channelPartnerId, TimeSelect dateTime);

    List<LocalDate> getDateBySelect(TimeSelect dateTime);

    List<LocalDateTime> getDate(TimeSelect dateTime);

    OrderStatisticsVO getOrderPrintType(Long channelPartnerId, TimeSelect dateTime);

    OrderAmountStatisticsVO getOrderAmountByOrderPrintType(Long channelPartnerId, TimeSelect dateTime);

    SingleOrderAmountStatisticsVO getSingleOrderAmount(Long channelPartnerId, TimeSelect dateTime);

    OrderIncomeRateVo getOrderIncomeRate(Long channelPartnerId, TimeSelect dateTime);

    List<DeviceRankVO> getDeviceRank(Long channelPartnerId, TimeSelect dateTime);

}
