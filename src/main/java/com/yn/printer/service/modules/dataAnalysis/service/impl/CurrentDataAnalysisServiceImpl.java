package com.yn.printer.service.modules.dataAnalysis.service.impl;

import cn.hutool.core.date.DateUtil;
import com.yn.printer.service.modules.advertisement.repository.PlacementPaymentRepository;
import com.yn.printer.service.modules.channel.entity.ChannelPartner;
import com.yn.printer.service.modules.channel.repository.ChannelPartnerRepository;
import com.yn.printer.service.modules.dataAnalysis.enums.TimeSelect;
import com.yn.printer.service.modules.dataAnalysis.service.ICurrentDataAnalysisService;
import com.yn.printer.service.modules.dataAnalysis.vo.*;
import com.yn.printer.service.modules.member.entity.Member;
import com.yn.printer.service.modules.operation.enums.DeviceStatus;
import com.yn.printer.service.modules.member.repository.MemberRepository;
import com.yn.printer.service.modules.operation.entity.DevicesList;
import com.yn.printer.service.modules.operation.enums.DeviceType;
import com.yn.printer.service.modules.operation.repository.DevicesListRepository;
import com.yn.printer.service.modules.operation.repository.TaskListRepository;
import com.yn.printer.service.modules.orders.entity.OrderManagement;
import com.yn.printer.service.modules.orders.enums.OrderPrintType;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CurrentDataAnalysisServiceImpl implements ICurrentDataAnalysisService {
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

    @Override
    public TotalDevicesVO totalData() {
        TotalDevicesVO totalDevicesVO = new TotalDevicesVO();

        List<DeviceStatus> deviceStatusList = Arrays.asList(DeviceStatus.ONLINE, DeviceStatus.RUN, DeviceStatus.ABNORMAL, DeviceStatus.OFFLINE);
        Long totalDevices = devicesListRepository.countTotalDevicesWithStatus(deviceStatusList);
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
        totalDevicesVO.setOnlineDevicesRate(Math.round(((double) onlineDevicesNumber / totalDevices * 100) * 100.0) / 100.0);
        totalDevicesVO.setAbnormalDevicesRate(Math.round(((double) abnormalDevicesNumber / totalDevices * 100) * 100.0) / 100.0);
        totalDevicesVO.setOfflineDevicesRate(Math.round(((double) offlineDevicesNumber / totalDevices * 100) * 100.0) / 100.0);
        totalDevicesVO.setRunDevicesRate(Math.round(((double) runDevicesNumber / totalDevices * 100) * 100.0) / 100.0);
        return totalDevicesVO;
    }

    @Override
    public Page<OrderVO> getTodayOrders(Pageable pageable) {
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime startOfDay = LocalDateTime.of(currentDate.toLocalDate(), LocalTime.MIN);

        Page<OrderManagement> byOrderDateBetween = orderManagementRepository.findByOrderDateBetweenOrderByOrderDateDesc(startOfDay, currentDate, pageable);

        List<OrderVO> orderVOList = byOrderDateBetween.getContent().stream()
                .map(orderManagement -> {
                    if (orderManagement == null) {
                        return null; // 或者抛出一个自定义的异常
                    }

                    OrderVO orderVO = new OrderVO();

                    TransactionStatus transactionStatus = orderManagement.getTransactionStatus();
                    if (transactionStatus != null) {
                        orderVO.setTransactionStatus(transactionStatus.getName());
                    }

                    orderVO.setOrderdate(DateUtil.formatLocalDateTime(orderManagement.getOrderDate()));

                    OrderPrintType orderPrintType = orderManagement.getOrderPrintType();
                    if (orderPrintType != null) {
                        orderVO.setOrderPrintType(orderPrintType.getValue());
                    }

                    Member orderer = orderManagement.getOrderer();
                    if (orderer != null) {
                        orderVO.setUserName(orderer.getName());
                    }

                    orderVO.setOrderAmount(orderManagement.getOrderAmount());

                    DevicesList device = orderManagement.getDevice();
                    if (device != null) {
                        orderVO.setDeviceName(device.getName());
                    }

                    return orderVO;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new PageImpl<>(orderVOList, pageable, byOrderDateBetween.getTotalElements());
    }

    @Override
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

        userAnalysisVO.setAddUserRate(Math.round(((double) addUserNumber / userNumber * 100) * 100.0) / 100.0);
        userAnalysisVO.setOldUserRate(Math.round(((double) oldUserNumber / userNumber * 100) * 100.0) / 100.0);
        return userAnalysisVO;
    }

    @Override
    public OrderTotalVO getOrderTotal() {
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime startOfDay = LocalDateTime.of(currentDate.toLocalDate(), LocalTime.MIN);
        OrderTotalVO orderTotalVO = new OrderTotalVO();
        Long completeOrderNumber = orderManagementRepository.countTodayOrdersByTransactionStatus(startOfDay, currentDate, TransactionStatus.COMPLETE);
        Long abnormalOrderNumber = orderManagementRepository.countTodayOrdersByTransactionStatus(startOfDay, currentDate, TransactionStatus.ABNORMAL);
        Long allOrderNumber = orderManagementRepository.countTodayOrders(startOfDay, currentDate);
        orderTotalVO.setCompleteOrderNumber(completeOrderNumber);
        orderTotalVO.setAbnormalOrderNumber(abnormalOrderNumber);
        orderTotalVO.setCompleteOrderRate(Math.round(((double) completeOrderNumber / allOrderNumber * 100) * 100.0) / 100.0);
        orderTotalVO.setAbnormalOrderRate(Math.round(((double) abnormalOrderNumber / allOrderNumber * 100) * 100.0) / 100.0);
        return orderTotalVO;
    }

    @Override
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

    @Override
    public Page<ChannelPartnerInfo> getAllChannelPartner(Pageable pageable) {
        List<ChannelPartnerInfo> allChannel = channelRepository.findAllChannel();
        System.out.println(allChannel);
        return new PageImpl<>(allChannel, pageable, allChannel.size());
    }

    @Override
    public List<DeviceStatisticsVO> getDeviceByChannelPartner(Long channelPartnerId, TimeSelect dateTime) {
        ArrayList<DeviceStatisticsVO> deviceStatisticsVoList = new ArrayList<>();
        DeviceStatus[] statuses = {DeviceStatus.OFFLINE, DeviceStatus.ONLINE, DeviceStatus.RUN, DeviceStatus.NOT_ACTIVE, DeviceStatus.ABNORMAL, DeviceStatus.STOP};
        for (DeviceStatus status : statuses) {
            DeviceStatisticsVO deviceStatisticsVO = new DeviceStatisticsVO();
            List<ChannelPartner> byName = channelRepository.findChannelById(channelPartnerId);
            deviceStatisticsVO.setTotalDeviceNumber(devicesListRepository.countByTerminalMerchantsAndStatusAndDeviceType(byName, status, Optional.empty()));
            deviceStatisticsVO.setSWJDeviceNumber(devicesListRepository.countByTerminalMerchantsAndStatusAndDeviceType(byName, status, Optional.of(DeviceType.SWJ)));
            deviceStatisticsVO.setDeviceState(status.getName());
            deviceStatisticsVO.setSNJDeviceNumber(devicesListRepository.countByTerminalMerchantsAndStatusAndDeviceType(byName, status, Optional.of(DeviceType.SNJ)));
            deviceStatisticsVoList.add(deviceStatisticsVO);
        }
        return deviceStatisticsVoList;
    }

    @Override
    public UserStatisticsVO getUserByChannelPartnerAndDateTime(Long channelPartnerId, TimeSelect dateTime) {
        List<LocalDateTime> date = getDate(dateTime);
        LocalDateTime startDateTime = date.get(0);
        LocalDateTime endDateTime = date.get(1);
        UserStatisticsVO userStatisticsVO = new UserStatisticsVO();
        List<ChannelPartner> byName = channelRepository.findChannelById(channelPartnerId);
        List<DevicesList> DevicesList = devicesListRepository.findByChannel(byName);
        long uUserNumber = orderManagementRepository.
                countDistinctOrderByOrderDateBetweenAndDeviceList(startDateTime, endDateTime, DevicesList);
        userStatisticsVO.setUserNumber(uUserNumber);
        long newUserNumber = orderManagementRepository.
                countNewUsersByOrderDateAndJoiningDateAndDeviceList(startDateTime, endDateTime, DevicesList);
        userStatisticsVO.setNewUserNumber(newUserNumber);
        long oldUserNumber = orderManagementRepository.countOldUsersByOrderDateAndJoiningDateAndDeviceList(startDateTime, endDateTime, DevicesList);
        userStatisticsVO.setOldUserNumber(oldUserNumber);
        userStatisticsVO.setNewUserRate(Math.round(((double) newUserNumber / uUserNumber * 100) * 100.0) / 100.0);
        userStatisticsVO.setOldUserRate(Math.round(((double) oldUserNumber / uUserNumber * 100) * 100.0) / 100.0);
        return userStatisticsVO;
    }

    @Override
    public List<LocalDate> getDateBySelect(TimeSelect dateTime) {
        List<LocalDateTime> date = getDate(dateTime);
        List<LocalDate> startAndEnd = new ArrayList<>();
        startAndEnd.add(date.get(0).toLocalDate());
        startAndEnd.add(date.get(1).toLocalDate());
        return startAndEnd;
    }

    @Override
    public List<LocalDateTime> getDate(TimeSelect dateTime) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;
        List<LocalDateTime> startAndEnd = new ArrayList<>();
        switch (dateTime) {
            case TODAY:
                startDateTime = now.toLocalDate().atStartOfDay();
//              endDateTime = LocalDateTime.of(now.toLocalDate(), LocalTime.MAX);
                endDateTime = LocalDateTime.now();
                break;
            case THIS_WEEK:
                startDateTime = now.with(DayOfWeek.MONDAY).toLocalDate().atStartOfDay();
//              endDateTime = now.with(DayOfWeek.SUNDAY).toLocalDate().atTime(LocalTime.MAX);
                endDateTime = LocalDateTime.now();
                break;
            case THIS_MONTH:
                startDateTime = now.with(TemporalAdjusters.firstDayOfMonth()).toLocalDate().atStartOfDay();
//              endDateTime = now.with(TemporalAdjusters.lastDayOfMonth()).toLocalDate().atTime(LocalTime.MAX);
                endDateTime = LocalDateTime.now();
                break;
            case THIS_YEAR:
                startDateTime = now.with(TemporalAdjusters.firstDayOfYear()).toLocalDate().atStartOfDay();
//              endDateTime = now.with(TemporalAdjusters.lastDayOfYear()).toLocalDate().atTime(LocalTime.MAX);
                endDateTime = LocalDateTime.now();
                break;
            default:
                break;
        }
        startAndEnd.add(startDateTime);
        startAndEnd.add(endDateTime);
        return startAndEnd;
    }

    @Override
    public OrderStatisticsVO getOrderPrintType(Long channelPartnerId, TimeSelect dateTime) {
        List<LocalDateTime> date = getDate(dateTime);
        LocalDateTime startDateTime = date.get(0);
        LocalDateTime endDateTime = date.get(1);
        List<ChannelPartner> byName = channelRepository.findChannelById(channelPartnerId);
        List<DevicesList> DevicesList = devicesListRepository.findByChannel(byName);
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setPhotoPrintNumber(orderManagementRepository
                .countByOrderPrintTypeAndOrderDateBetweenAndDeviceIn(OrderPrintType.PHOTO, startDateTime, endDateTime, DevicesList));
        orderStatisticsVO.setDocumentPrintNumber(orderManagementRepository
                .countByOrderPrintTypeAndOrderDateBetweenAndDeviceIn(OrderPrintType.DOCUMENT, startDateTime, endDateTime, DevicesList));
        orderStatisticsVO.setDocumentAndPhotoPrintNumber(orderManagementRepository
                .countByOrderPrintTypeAndOrderDateBetweenAndDeviceIn(OrderPrintType.DOCUMENT_PHOTO, startDateTime, endDateTime, DevicesList));
        String[] orderPrintTypes = {OrderPrintType.PHOTO.getValue(), OrderPrintType.DOCUMENT.getValue(), OrderPrintType.DOCUMENT_PHOTO.getValue()};
        orderStatisticsVO.setOrderPrintType(Arrays.asList(orderPrintTypes));
        return orderStatisticsVO;
    }

    @Override
    public OrderAmountStatisticsVO getOrderAmountByOrderPrintType(Long channelPartnerId, TimeSelect dateTime) {
        List<LocalDateTime> date = getDate(dateTime);
        LocalDateTime startDateTime = date.get(0);
        LocalDateTime endDateTime = date.get(1);
        OrderAmountStatisticsVO orderAmountStatisticsVO = new OrderAmountStatisticsVO();
        List<ChannelPartner> byName = channelRepository.findChannelById(channelPartnerId);
        List<DevicesList> DevicesList = devicesListRepository.findByChannel(byName);
        orderAmountStatisticsVO.setPhotoPrintAmount(orderManagementRepository
                .sumOrderAmountByOrderPrintTypeAndOrderDateBetweenAndDeviceIn(OrderPrintType.PHOTO, startDateTime, endDateTime, DevicesList));
        orderAmountStatisticsVO.setDocumentPrintAmount(orderManagementRepository
                .sumOrderAmountByOrderPrintTypeAndOrderDateBetweenAndDeviceIn(OrderPrintType.DOCUMENT, startDateTime, endDateTime, DevicesList));
        orderAmountStatisticsVO.setDocumentAndPhotoPrintAmount(orderManagementRepository
                .sumOrderAmountByOrderPrintTypeAndOrderDateBetweenAndDeviceIn(OrderPrintType.DOCUMENT_PHOTO, startDateTime, endDateTime, DevicesList));
        String[] orderPrintTypes = {OrderPrintType.PHOTO.getValue(), OrderPrintType.DOCUMENT.getValue(), OrderPrintType.DOCUMENT_PHOTO.getValue()};
        orderAmountStatisticsVO.setOrderPrintType(Arrays.asList(orderPrintTypes));
        return orderAmountStatisticsVO;
    }

    @Override
    public SingleOrderAmountStatisticsVO getSingleOrderAmount(Long channelPartnerId, TimeSelect dateTime) {
        List<LocalDateTime> date = getDate(dateTime);
        LocalDateTime startDateTime = date.get(0);
        LocalDateTime endDateTime = date.get(1);
        List<ChannelPartner> byName = channelRepository.findChannelById(channelPartnerId);
        List<DevicesList> DevicesList = devicesListRepository.findByChannel(byName);
        SingleOrderAmountStatisticsVO singleOrderAmountStatisticsVO = new SingleOrderAmountStatisticsVO();

        singleOrderAmountStatisticsVO.setOneToTwoYuan(orderManagementRepository.
                countByOrderDateAndOrderAmountAndDeviceIn(startDateTime, endDateTime,
                        BigDecimal.valueOf(1.00), BigDecimal.valueOf(2.00), DevicesList));

        singleOrderAmountStatisticsVO.setTwoToFourYuan(orderManagementRepository.
                countByOrderDateAndOrderAmountAndDeviceIn(startDateTime, endDateTime,
                        BigDecimal.valueOf(2.00), BigDecimal.valueOf(4.00), DevicesList));

        singleOrderAmountStatisticsVO.setFourToTenYuan(orderManagementRepository.
                countByOrderDateAndOrderAmountAndDeviceIn(startDateTime, endDateTime,
                        BigDecimal.valueOf(4.00), BigDecimal.valueOf(10.00), DevicesList));

        singleOrderAmountStatisticsVO.setTenToTwentyYuan(orderManagementRepository.
                countByOrderDateAndOrderAmountAndDeviceIn(startDateTime, endDateTime,
                        BigDecimal.valueOf(10.00), BigDecimal.valueOf(20.00), DevicesList));
        singleOrderAmountStatisticsVO.setOneYuanLess(
                orderManagementRepository.countByOrderDateAndOrderAmountLessAndDeviceIn(startDateTime, endDateTime,
                        BigDecimal.valueOf(0.00), BigDecimal.valueOf(1.00), DevicesList));
        singleOrderAmountStatisticsVO.setTwentyYuanMore(
                orderManagementRepository.countByOrderDateAndOrderAmountMoreAndDeviceIn(startDateTime, endDateTime,
                        BigDecimal.valueOf(20.00), DevicesList));
        return singleOrderAmountStatisticsVO;
    }

    @Override
    public OrderIncomeRateVo getOrderIncomeRate(Long channelPartnerId, TimeSelect dateTime) {
        List<LocalDateTime> date = getDate(dateTime);
        LocalDateTime startDateTime = date.get(0);
        LocalDateTime endDateTime = date.get(1);
        List<ChannelPartner> byName = channelRepository.findChannelById(channelPartnerId);
        List<DevicesList> DevicesList = devicesListRepository.findByChannel(byName);
        OrderIncomeRateVo orderIncomeRateVo = new OrderIncomeRateVo();
        BigDecimal totalIncome = Optional.ofNullable(orderManagementRepository
                .sumPaymentAmountByOrderDateAndDeviceAndpayStatusAndTransactionStatus(
                        startDateTime, endDateTime, DevicesList, PayStatus.PAID, TransactionStatus.COMPLETE)).orElse(BigDecimal.ZERO);

        long totalIncomeOrder = Optional.ofNullable(orderManagementRepository
                .countByOrderDateAndDeviceAndpayStatusAndTransactionStatus(
                        startDateTime, endDateTime, DevicesList, PayStatus.PAID, TransactionStatus.COMPLETE)).orElse(0L);

        BigDecimal cancelIncome = Optional.ofNullable(orderManagementRepository
                .sumOrderAmountByOrderDateAndDeviceAndTransactionStatus(
                        startDateTime, endDateTime, DevicesList, TransactionStatus.CANCELED)).orElse(BigDecimal.ZERO);

        long cancelOrder = Optional.ofNullable(orderManagementRepository
                .countByOrderDateAndDeviceAndTransactionStatus(
                        startDateTime, endDateTime, DevicesList, TransactionStatus.CANCELED)).orElse(0L);

        BigDecimal abnormalIncome = Optional.ofNullable(orderManagementRepository
                .sumOrderAmountByOrderDateAndDeviceAndTransactionStatus(
                        startDateTime, endDateTime, DevicesList, TransactionStatus.ABNORMAL)).orElse(BigDecimal.ZERO);

        long abnormalOrder = Optional.ofNullable(orderManagementRepository
                .countByOrderDateAndDeviceAndTransactionStatus(
                        startDateTime, endDateTime, DevicesList, TransactionStatus.ABNORMAL)).orElse(0L);

        orderIncomeRateVo.setTotalIncome(totalIncome);
        orderIncomeRateVo.setTotalIncomeOrder(totalIncomeOrder);
        orderIncomeRateVo.setCancelIncome(cancelIncome);
        orderIncomeRateVo.setCancelOrder(cancelOrder);
        orderIncomeRateVo.setAbnormalIncome(abnormalIncome);
        orderIncomeRateVo.setAbnormalOrder(abnormalOrder);

        BigDecimal totalAmount = totalIncome.add(cancelIncome).add(abnormalIncome);
        double totalIncomeRate = totalAmount.compareTo(BigDecimal.ZERO) == 0 ? 0.0 : totalIncome.divide(totalAmount, 4, RoundingMode.HALF_UP).doubleValue() * 100;
        double cancelIncomeRate = totalAmount.compareTo(BigDecimal.ZERO) == 0 ? 0.0 : cancelIncome.divide(totalAmount, 4, RoundingMode.HALF_UP).doubleValue() * 100;
        double abnormalIncomeRate = totalAmount.compareTo(BigDecimal.ZERO) == 0 ? 0.0 : abnormalIncome.divide(totalAmount, 4, RoundingMode.HALF_UP).doubleValue() * 100;

        orderIncomeRateVo.setTotalIncomeRate(totalIncomeRate);
        orderIncomeRateVo.setAbnormalIncomeRate(abnormalIncomeRate);
        orderIncomeRateVo.setCancelIncomeRate(cancelIncomeRate);

        long totalOrders = totalIncomeOrder + cancelOrder + abnormalOrder;
        double totalOrderRate = totalOrders == 0 ? 0.0 : (double) totalIncomeOrder / totalOrders * 100;
        double cancelOrderRate = totalOrders == 0 ? 0.0 : (double) cancelOrder / totalOrders * 100;
        double abnormalOrderRate = totalOrders == 0 ? 0.0 : (double) abnormalOrder / totalOrders * 100;

        orderIncomeRateVo.setTotalIncomeOrderRate(totalOrderRate);
        orderIncomeRateVo.setCancelOrderRate(cancelOrderRate);
        orderIncomeRateVo.setAbnormalOrderRate(abnormalOrderRate);

        orderIncomeRateVo.setOrderAveragePrice(BigDecimal.valueOf(totalOrders).equals(BigDecimal.ZERO) ?
                BigDecimal.ZERO : totalAmount.divide(BigDecimal.valueOf(totalOrders), 2, RoundingMode.HALF_UP));
        return orderIncomeRateVo;
    }

    @Override
    public List<DeviceRankVO> getDeviceRank(Long channelPartnerId, TimeSelect dateTime) {
        List<LocalDateTime> date = getDate(dateTime);
        LocalDateTime startDateTime = date.get(0);
        LocalDateTime endDateTime = date.get(1);
        List<ChannelPartner> byName = channelRepository.findChannelById(channelPartnerId);
        List<DevicesList> DevicesList = devicesListRepository.findByChannel(byName);
        return orderManagementRepository
                .sumPaymentAmountByOrderDateAndDeviceRankAndpayStatusAndTransactionStatus(startDateTime,
                        endDateTime, DevicesList);
    }

}

