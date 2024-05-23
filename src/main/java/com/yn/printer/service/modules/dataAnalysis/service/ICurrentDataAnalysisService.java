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

    List<DeviceStatisticsVO> getDeviceByChannelPartner(Long channelPartnerId);

    UserStatisticsVO getUserByChannelPartnerAndDateTime(Long channelPartnerId, LocalDate startDate,LocalDate endDate);

    List<LocalDate> getDateBySelect(TimeSelect dateTime);

    List<LocalDateTime> getDate(TimeSelect dateTime);

    OrderStatisticsVO getOrderPrintType(Long channelPartnerId, LocalDate startDate,LocalDate endDate);

    OrderAmountStatisticsVO getOrderAmountByOrderPrintType(Long channelPartnerId, LocalDate startDate, LocalDate endDate);

    SingleOrderAmountStatisticsVO getSingleOrderAmount(Long channelPartnerId, LocalDate startDate, LocalDate endDate);

    OrderIncomeRateVo getOrderIncomeRate(Long channelPartnerId, LocalDate startDate, LocalDate endDate);

    List<DeviceRankVO> getDeviceRank(Long channelPartnerId, LocalDate startDate, LocalDate endDate);

}
