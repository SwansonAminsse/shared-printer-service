package com.yn.printer.service.modules.dataAnalysis.service;

import cn.hutool.core.date.DateUtil;
import com.yn.printer.service.modules.advertisement.repository.PlacementPaymentRepository;
import com.yn.printer.service.modules.channel.repository.ChannelPartnerRepository;
import com.yn.printer.service.modules.dataAnalysis.vo.*;
import com.yn.printer.service.modules.enums.DeviceStatus;
import com.yn.printer.service.modules.member.repository.MemberRepository;
import com.yn.printer.service.modules.operation.repository.DevicesListRepository;
import com.yn.printer.service.modules.operation.repository.TaskListRepository;
import com.yn.printer.service.modules.orders.entity.OrderManagement;
import com.yn.printer.service.modules.orders.enums.PayStatus;
import com.yn.printer.service.modules.orders.enums.TransactionStatus;
import com.yn.printer.service.modules.orders.repository.OrderManagementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CurrentDataAnalysisService {
    @Autowired
    private ChannelPartnerRepository channelRepository;
    @Autowired
    private DevicesListRepository devicesListRepository;
    @Autowired
    private OrderManagementRepository orderManagementRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TaskListRepository taskListRepository;
    @Autowired
    private PlacementPaymentRepository placementPaymentRepository;

    public TotalDevicesVO totalData() {
        TotalDevicesVO totalDevicesVO = new TotalDevicesVO();
        Long totalDevices = devicesListRepository.countTotalDevicesWithStatus();
        totalDevicesVO.setDevicesNumber(totalDevices);
        totalDevicesVO.setDeviceIncome(Optional.ofNullable(orderManagementRepository.sumTotalOrderIncome(PayStatus.PAID)).orElse(BigDecimal.ZERO));
        totalDevicesVO.setUserNumber(memberRepository.countByStatus(true));
        Long onlineDevicesNumber = Optional.ofNullable(devicesListRepository.countByStatus(DeviceStatus.ONLINE)).orElse(0L);
        totalDevicesVO.setOnlineDevicesNumber(onlineDevicesNumber);
        Long runDevicesNumber = Optional.ofNullable(devicesListRepository.countByStatus(DeviceStatus.RUN)).orElse(0L);
        totalDevicesVO.setRunDevicesNumber(runDevicesNumber);
        Long abnormalDevicesNumber = Optional.ofNullable(devicesListRepository.countByStatus(DeviceStatus.ABNORMAL)).orElse(0L);
        totalDevicesVO.setAbnormalDevicesNumber(abnormalDevicesNumber);
        Long offlineDevicesNumber = Optional.ofNullable(devicesListRepository.countByStatus(DeviceStatus.OFFLINE)).orElse(0L);
        totalDevicesVO.setOfflineDevicesNumber(offlineDevicesNumber);
        totalDevicesVO.setOnlineDevicesRate((double) onlineDevicesNumber / totalDevices * 100);
        totalDevicesVO.setAbnormalDevicesRate((double) abnormalDevicesNumber / totalDevices * 100);
        totalDevicesVO.setOfflineDevicesRate((double) offlineDevicesNumber / totalDevices * 100);
        totalDevicesVO.setRunDevicesRate((double) runDevicesNumber / totalDevices * 100);
        return totalDevicesVO;
    }

    public Page<OrderVO> getTodayOrders(Pageable pageable) {
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime startOfDay = LocalDateTime.of(currentDate.toLocalDate(), LocalTime.MIN);
        Page<OrderManagement> byOrderDateBetween = orderManagementRepository.findByOrderDateBetween(startOfDay, currentDate, pageable);
        List<OrderVO> orderVOList = byOrderDateBetween.getContent().stream()
                .map(orderManagement -> {
                    OrderVO orderVO = new OrderVO();
                    orderVO.setTransactionStatus(orderManagement.getTransactionStatus().getName());
                    orderVO.setOrderdate(DateUtil.formatLocalDateTime(orderManagement.getOrderDate()));
                    orderVO.setOrderPrintType(orderManagement.getOrderPrintType().getValue());
                    orderVO.setUserName(orderManagement.getOrderer().getName());
                    orderVO.setOrderAmount(orderManagement.getOrderAmount());
                    orderVO.setDeviceName(orderManagement.getDevice().getName());
                    return orderVO;
                })
                .collect(Collectors.toList());
        return new PageImpl<>(orderVOList, pageable, byOrderDateBetween.getTotalElements());
    }

    public UserAnalysisVO getUserTodayAnalysis() {
        UserAnalysisVO userAnalysisVO = new UserAnalysisVO();
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime startOfDay = LocalDateTime.of(currentDate.toLocalDate(), LocalTime.MIN);
        Long addUserNumber = Optional.ofNullable(memberRepository.countByJoiningDate(currentDate.toLocalDate())).orElse(0L);
        Long userNumber = memberRepository.countByStatus(true);
        Long oldUserNumber = userNumber - addUserNumber;
        userAnalysisVO.setUserNumber(userNumber);
        userAnalysisVO.setAddUserNumber(addUserNumber);
        userAnalysisVO.setOldUserNumber(oldUserNumber);
        userAnalysisVO.setAddUserRate((double) addUserNumber / userNumber * 100);
        userAnalysisVO.setOldUserRate((double) oldUserNumber / userNumber * 100);
        return userAnalysisVO;
    }

    public OrderTotalVO getOrderTotal() {
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime startOfDay = LocalDateTime.of(currentDate.toLocalDate(), LocalTime.MIN);
        OrderTotalVO orderTotalVO = new OrderTotalVO();
        Long completeOrderNumber = orderManagementRepository.countTodayOrdersByTransactionStatus(startOfDay, currentDate, TransactionStatus.COMPLETE);
        Long abnormalOrderNumber = orderManagementRepository.countTodayOrdersByTransactionStatus(startOfDay, currentDate, TransactionStatus.ABNORMAL);
        Long allOrderNumber = orderManagementRepository.countTodayOrders(startOfDay, currentDate);
        orderTotalVO.setCompleteOrderNumber(completeOrderNumber);
        orderTotalVO.setAbnormalOrderNumber(abnormalOrderNumber);
        orderTotalVO.setCompleteOrderRate((double) completeOrderNumber / allOrderNumber * 100);
        orderTotalVO.setAbnormalOrderRate((double) abnormalOrderNumber / allOrderNumber * 100);
        return orderTotalVO;
    }

    public IncometotalVO getIncomeTotal() {
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime startOfDay = LocalDateTime.of(currentDate.toLocalDate(), LocalTime.MIN);
        DayOfWeek currentDayOfWeek = currentDate.getDayOfWeek();
        LocalDate lastWeekSameDay = LocalDate.from(currentDate.minusWeeks(1).with(TemporalAdjusters.previousOrSame(currentDayOfWeek)));
        LocalDateTime lastWeekSameDayTime = lastWeekSameDay.atStartOfDay();
        LocalDateTime lastWeekSameDayEnd = lastWeekSameDay.atTime(LocalTime.MAX);
        LocalDateTime startOfYesterday = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIN);
        LocalDateTime yesterdayEnd = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MAX);
        IncometotalVO incometotalVO = new IncometotalVO();
        BigDecimal incomeTotal = Optional.ofNullable(orderManagementRepository.sumByDeviceIncome(startOfDay, currentDate, PayStatus.PAID)).orElse(BigDecimal.ZERO);
        incometotalVO.setDeviceIncome(incomeTotal);
        orderManagementRepository.sumByDeviceIncome(startOfYesterday, startOfDay, PayStatus.PAID);
        BigDecimal yesterdayIncome = Optional.ofNullable(orderManagementRepository.sumByDeviceIncome(startOfYesterday, yesterdayEnd, PayStatus.PAID)).orElse(BigDecimal.ZERO);
        BigDecimal lastWeekSameDayTimeIncome = Optional.ofNullable(orderManagementRepository.sumByDeviceIncome(lastWeekSameDayTime, lastWeekSameDayEnd, PayStatus.PAID)).orElse(BigDecimal.ZERO);
        lastWeekSameDayTimeIncome = (lastWeekSameDayTimeIncome != null) ? lastWeekSameDayTimeIncome : BigDecimal.ZERO;
        BigDecimal yesterdayRate;
        if (yesterdayIncome.compareTo(BigDecimal.ZERO) == 0 && incomeTotal.compareTo(BigDecimal.ZERO) == 0) {
            yesterdayRate = BigDecimal.ZERO;
        } else if (yesterdayIncome.compareTo(BigDecimal.ZERO) == 0) {
            yesterdayRate = new BigDecimal(100);
        } else {
            yesterdayRate = incomeTotal.subtract(yesterdayIncome)
                    .divide(yesterdayIncome, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(100));
        }
        BigDecimal lastWeekSameDayTimeIncomeRate;
        if (lastWeekSameDayTimeIncome.compareTo(BigDecimal.ZERO) == 0 && incomeTotal.compareTo(BigDecimal.ZERO) == 0) {
            lastWeekSameDayTimeIncomeRate = BigDecimal.ZERO;
        } else if (lastWeekSameDayTimeIncome.compareTo(BigDecimal.ZERO) == 0) {
            lastWeekSameDayTimeIncomeRate = new BigDecimal(100);
        } else {
            lastWeekSameDayTimeIncomeRate = incomeTotal.subtract(lastWeekSameDayTimeIncome)
                    .divide(lastWeekSameDayTimeIncome, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(100));
        }
        incometotalVO.setLastWeekRate(lastWeekSameDayTimeIncomeRate.doubleValue());
        incometotalVO.setYesterdayRate(yesterdayRate.doubleValue());
        return incometotalVO;
    }
}
