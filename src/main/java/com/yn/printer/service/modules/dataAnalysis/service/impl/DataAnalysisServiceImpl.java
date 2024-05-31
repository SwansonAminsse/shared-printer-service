package com.yn.printer.service.modules.dataAnalysis.service.impl;

import com.yn.printer.service.modules.advertisement.repository.PlacementPaymentRepository;
import com.yn.printer.service.modules.channel.repository.ChannelPartnerRepository;
import com.yn.printer.service.modules.dataAnalysis.service.IDataAnalysisService;
import com.yn.printer.service.modules.dataAnalysis.unitl.CustomTemporalAdjusters;
import com.yn.printer.service.modules.dataAnalysis.vo.*;
import com.yn.printer.service.modules.member.repository.MemberRepository;
import com.yn.printer.service.modules.operation.entity.DevicesList;
import com.yn.printer.service.modules.operation.repository.DevicesListRepository;
import com.yn.printer.service.modules.operation.repository.TaskListRepository;
import com.yn.printer.service.modules.orders.entity.OrderManagement;
import com.yn.printer.service.modules.orders.enums.TransactionStatus;
import com.yn.printer.service.modules.orders.repository.OrderManagementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import static com.yn.printer.service.modules.channel.enums.ChannelType.*;
import static com.yn.printer.service.modules.orders.enums.PayStatus.PAID;

@Service
public class DataAnalysisServiceImpl implements IDataAnalysisService {
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
    public TotalChannelVO getTotalChannel() {
        return new TotalChannelVO(channelRepository.countByChannelType(PrimaryChannel), channelRepository.countByChannelType(SecondaryChannel), channelRepository.countByChannelType(TerminalChannel));
    }

    @Override
    public IncomeSumVO totalIncome(Boolean time) {
        if (time) {
            LocalDateTime startOfDate = LocalDateTime.of(LocalDate.now().withDayOfMonth(1), LocalTime.MIN);//本月第一天
        }
        LocalDateTime startOfDate = LocalDateTime.of(LocalDate.now().with(CustomTemporalAdjusters.firstDayOfQuarter()), LocalTime.MIN);//本季度第一天
        LocalDateTime endOfDate = LocalDateTime.now();
        IncomeSumVO incomeSumVO = new IncomeSumVO();
        incomeSumVO.setDevicesNumber(devicesListRepository.countByDeviceStatus(true));
        incomeSumVO.setDeviceIncome(orderManagementRepository.sumByDeviceIncome(startOfDate, endOfDate, PAID, TransactionStatus.COMPLETE));
        List<DevicesList> devicesLists = devicesListRepository.findByDeviceStatus(true);
        BigDecimal totalTzIncome = BigDecimal.ZERO;
        for (DevicesList devicesList : devicesLists) {
            BigDecimal tzRatio = BigDecimal.valueOf(100 - devicesList.getCityPartnerRatio() - devicesList.getPartnersRatio() - devicesList.getTerminalMerchantsRatio());
            for (OrderManagement orderManagement : orderManagementRepository.findByDeviceAndPayStatusAndOrderDateBetween(devicesList, PAID, startOfDate, endOfDate)) {
                BigDecimal currentTzIncome = orderManagement.getPaymentAmount().multiply(tzRatio).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                totalTzIncome = totalTzIncome.add(currentTzIncome);
            }
        }
        incomeSumVO.setSettlementAmount(totalTzIncome);
        return incomeSumVO;
    }

    @Override
    public UserTotalVO getUserTotal(Boolean time) {
        if (time) {
            LocalDateTime startOfDate = LocalDateTime.of(LocalDate.now().withDayOfMonth(1), LocalTime.MIN);//本月第一天
        }
        LocalDateTime startOfDate = LocalDateTime.of(LocalDate.now().with(CustomTemporalAdjusters.firstDayOfQuarter()), LocalTime.MIN);//本季度第一天
        LocalDateTime endOfDate = LocalDateTime.now();
        UserTotalVO userTotalVO = new UserTotalVO();
        long addUserNumber = orderManagementRepository.countNewUsersByOrderDate(startOfDate, endOfDate);
        long userNumber = memberRepository.countByStatus(true);
        long userDealNumber = orderManagementRepository.countDistinctOrderByOrderDateBetween(startOfDate, endOfDate);
        userTotalVO.setUserNumber(userNumber);
        userTotalVO.setUserDealNumber(userDealNumber);
        userTotalVO.setAddUserNumber(addUserNumber);
        double addUserRate = (addUserNumber != 0 && userNumber != 0) ? ((double) userNumber / userNumber) * 100 : (userNumber == 0 ? 100.0 : 0.0);
        userTotalVO.setAddUserRate(addUserRate);
        long ordersNumber = orderManagementRepository.countOrdersByOrderDateBetween(startOfDate, endOfDate);
        userTotalVO.setOrdersNumber(ordersNumber);
        Long repeatBuyers = orderManagementRepository.countRepeatBuyersByOrderDate(startOfDate, endOfDate);
        if (userNumber != 0 && repeatBuyers != null) {
            double customerRepurchaseRate = repeatBuyers != 0 ? (double) repeatBuyers / userNumber * 100 : 0.0;
            userTotalVO.setCustomerRepurchaseRate(customerRepurchaseRate);
        }
        long ordersTotalNumber = orderManagementRepository.countByPayStatus(PAID);
        double ordersGrowthRate = (ordersNumber != 0 && ordersTotalNumber != 0) ? ((double) ordersNumber / ordersTotalNumber) * 100 : 0.0;
        userTotalVO.setOrdersGrowthRate(ordersGrowthRate);
        return userTotalVO;
    }

    @Override
    public DevicesDataVO getDevicesData(Boolean time) {
        if (time) {
            LocalDateTime startOfDate = LocalDateTime.of(LocalDate.now().withDayOfMonth(1), LocalTime.MIN);//本月第一天
        }
        LocalDateTime startOfDate = LocalDateTime.of(LocalDate.now().with(CustomTemporalAdjusters.firstDayOfQuarter()), LocalTime.MIN);//本季度第一天
        LocalDateTime endOfDate = LocalDateTime.now();
        DevicesDataVO devicesDataVO = new DevicesDataVO();
        devicesDataVO.setDevicesNumber(devicesListRepository.countByDeviceStatus(true));
        devicesDataVO.setNewDevicesNumber(devicesListRepository.countByCreatedOnAfter(startOfDate));
        devicesDataVO.setUseDevicesNumber(orderManagementRepository.countOrdersByOrderDateBetween(startOfDate, endOfDate));
        devicesDataVO.setTaskDevicesNumber(taskListRepository.countByCompletionTimeAfter(startOfDate));
        BigDecimal turnoverNumber = orderManagementRepository.sumByDeviceIncome(startOfDate, endOfDate, PAID, TransactionStatus.COMPLETE);
        devicesDataVO.setOrdersTurnoverNumber(orderManagementRepository.sumByDeviceIncome(startOfDate, endOfDate, PAID, TransactionStatus.COMPLETE));
        BigDecimal oldTurnoverNumber = orderManagementRepository.sumByOldDeviceIncome(startOfDate, PAID);
        if (turnoverNumber != null && oldTurnoverNumber != null) {
            double turnoverRate = (oldTurnoverNumber.compareTo(BigDecimal.ZERO) != 0) ? turnoverNumber.subtract(oldTurnoverNumber).divide(oldTurnoverNumber, 2, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).doubleValue() : oldTurnoverNumber.compareTo(BigDecimal.ZERO) == 0 ? 100.0 : 0.0;
            devicesDataVO.setTurnoverRate(turnoverRate);
        }
        BigDecimal oldAdvertisementPayment = placementPaymentRepository.getADVERTISEMENTPayment(startOfDate);
        BigDecimal advertisementPayment = placementPaymentRepository.getADVERTISEMENTPaymentDateBetween(startOfDate, endOfDate);
        if (oldAdvertisementPayment != null && advertisementPayment != null) {
            double advertisementRate = (oldAdvertisementPayment.compareTo(BigDecimal.ZERO) != 0) ? advertisementPayment.subtract(oldAdvertisementPayment).divide(oldAdvertisementPayment, 2, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).doubleValue() : oldAdvertisementPayment.compareTo(BigDecimal.ZERO) == 0 ? 100.0 : 0.0;
            devicesDataVO.setAdvertisementRate(advertisementRate);
        }
        return devicesDataVO;
    }

    @Override
    public UsersAndVolumeOfWeekVO getUsersAndVolumeOfWeekVO() {
        List<Long> userDealNumber = new ArrayList<>();
        List<BigDecimal> ordersTurnoverNumber = new ArrayList<>();
        List<LocalDateTime> daysOfWeek = new ArrayList<>();
        LocalDate currentMonday = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate currentDate = LocalDate.now();
        for (DayOfWeek day : DayOfWeek.values()) {
            if (day.getValue() >= DayOfWeek.MONDAY.getValue() && day.getValue() <= DayOfWeek.SUNDAY.getValue()) {
                LocalDate dateToCheck = currentMonday.plusDays(day.getValue() - DayOfWeek.MONDAY.getValue());
                if (dateToCheck.isBefore(currentDate) || dateToCheck.isEqual(currentDate)) {
                    LocalDateTime dateTime = dateToCheck.atStartOfDay(ZoneId.systemDefault()).toLocalDateTime();
                    daysOfWeek.add(dateTime);
                } else {
                    break;
                }
            }
        }
        for (LocalDateTime startOfDate : daysOfWeek) {
            long ordersNumber = orderManagementRepository.countOrdersByOrderDateBetween(startOfDate, startOfDate.with(LocalTime.MAX));
            BigDecimal orderTurnoverNumber = orderManagementRepository.sumByDeviceIncome(startOfDate, startOfDate.with(LocalTime.MAX), PAID, TransactionStatus.COMPLETE);
            userDealNumber.add(ordersNumber);
            ordersTurnoverNumber.add(orderTurnoverNumber);
        }
        UsersAndVolumeOfWeekVO usersAndVolumeOfWeekVO = new UsersAndVolumeOfWeekVO();
        usersAndVolumeOfWeekVO.setUserDealNumber(userDealNumber);
        usersAndVolumeOfWeekVO.setOrdersTurnoverNumber(ordersTurnoverNumber);
        return usersAndVolumeOfWeekVO;
    }

    @Override
    public List<BigDecimal> getGrossProfit() {
        List<BigDecimal> grossProfit = new ArrayList<>();
        List<LocalDateTime> daysOfWeek = new ArrayList<>();
        LocalDate currentMonday = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate currentDate = LocalDate.now();
        for (DayOfWeek day : DayOfWeek.values()) {
            if (day.getValue() >= DayOfWeek.MONDAY.getValue() && day.getValue() <= DayOfWeek.SUNDAY.getValue()) {
                LocalDate dateToCheck = currentMonday.plusDays(day.getValue() - DayOfWeek.MONDAY.getValue());
                if (dateToCheck.isBefore(currentDate) || dateToCheck.isEqual(currentDate)) {
                    LocalDateTime dateTime = dateToCheck.atStartOfDay(ZoneId.systemDefault()).toLocalDateTime();
                    daysOfWeek.add(dateTime);
                } else {
                    break;
                }
            }
        }
        for (LocalDateTime startOfDate : daysOfWeek) {
            grossProfit.add(orderManagementRepository.sumByDeviceIncome(startOfDate, startOfDate.with(LocalTime.MAX), PAID, TransactionStatus.COMPLETE));
        }
        return grossProfit;
    }
}