package com.yn.printer.service.modules.settlement.service.impl;

import com.yn.printer.service.interceptor.AuditInterceptor;
import com.yn.printer.service.modules.channel.entity.ChannelPartner;
import com.yn.printer.service.modules.channel.entity.ChannelUser;
import com.yn.printer.service.modules.channel.repository.ChannelPartnerRepository;
import com.yn.printer.service.modules.channel.repository.ChannelUserRepository;
import com.yn.printer.service.modules.member.entity.Member;
import com.yn.printer.service.modules.operation.entity.DevicesList;
import com.yn.printer.service.modules.operation.repository.DevicesListRepository;
import com.yn.printer.service.modules.operation.util.ChannelUtils;
import com.yn.printer.service.modules.orders.repository.OrderManagementRepository;
import com.yn.printer.service.modules.settlement.entity.ChannelSettlement;
import com.yn.printer.service.modules.settlement.entity.Equipment;
import com.yn.printer.service.modules.settlement.entity.SettlementDetails;
import com.yn.printer.service.modules.settlement.entity.SettlementTime;
import com.yn.printer.service.modules.settlement.enums.Month;
import com.yn.printer.service.modules.settlement.repository.ChannelSettlementRepository;
import com.yn.printer.service.modules.settlement.repository.EquipmentRepository;
import com.yn.printer.service.modules.settlement.repository.SettlementDetailsRepository;
import com.yn.printer.service.modules.settlement.repository.SettlementTimeRepository;
import com.yn.printer.service.modules.settlement.service.ISettlementDetailsService;
import com.yn.printer.service.modules.settlement.vo.IncomeSumVO;
import com.yn.printer.service.modules.settlement.vo.IncomeVO;
import com.yn.printer.service.modules.settlement.vo.UserTotalVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.yn.printer.service.modules.orders.enums.PayStatus.PAID;

@Slf4j
@Service
public class SettlementDetailsServiceImpl implements ISettlementDetailsService {
    @Autowired
    private SettlementDetailsRepository settlementDetailsRepository;
    @Autowired
    private ChannelUserRepository channelUserRepository;
    @Autowired
    private DevicesListRepository devicesListRepository;
    @Autowired
    private OrderManagementRepository orderManagementRepository;
    @Autowired
    private ChannelPartnerRepository channelPartnerRepository;
    @Autowired
    private ChannelUtils channelUtils;
    @Autowired
    private SettlementTimeRepository settlementTimeRepository;
    @Autowired
    private ChannelSettlementRepository channelSettlementRepository;
    @Autowired
    private EquipmentRepository equipmentRepository;

    @Override
    public IncomeSumVO getDeviceIncomeSum(LocalDate startDate, LocalDate endDate) {
        return settlementDetailsRepository.findTotalIncomeAndAverageSettlementRatio(startDate, endDate, channelUtils.getChannelPartner(AuditInterceptor.CURRENT_MEMBER.get()));
    }

    @Override
    public Page<IncomeVO> getDeviceIncome(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return settlementDetailsRepository.findIncomeListWithCost(startDate, endDate, channelUtils.getChannelPartner(AuditInterceptor.CURRENT_MEMBER.get()), pageable);
    }

    @Override
    public Boolean addUser(String userName, String userPhone) {
        Member member = AuditInterceptor.CURRENT_MEMBER.get();
        String phoneNumber = member.getPhoneNumber();
        ChannelUser channelUser = channelUserRepository.findFirstByContactPhone(phoneNumber);
        ChannelPartner channelPartner = new ChannelPartner();
        if (channelUser == null) {
            channelPartner = channelPartnerRepository.findByAdminPhone(userPhone);
        } else {
            channelPartner = channelUser.getChannelPartner();
        }
        ChannelUser newChannelUser = new ChannelUser();
        newChannelUser.setChannelPartner(channelPartner);
        newChannelUser.setContactPhone(userPhone);
        newChannelUser.setName(userName);
        newChannelUser.setChannelUser(channelUser);
        channelUserRepository.save(newChannelUser);
        return true;
    }

    @Override
    public Page<UserTotalVO> userTotal(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        ChannelPartner channelPartner = channelUtils.getChannelPartner(AuditInterceptor.CURRENT_MEMBER.get());
        Page<DevicesList> devicesLists = devicesListRepository.findDevicesByChannelPartner(channelPartner, pageable);
        LocalDateTime startOfDate = startDate.atStartOfDay();
        LocalDateTime endOfDay = endDate.atTime(LocalTime.MAX);

        List<UserTotalVO> userTotalVOList = new ArrayList<>();

        for (DevicesList device : devicesLists.getContent()) {
            Long userNumber = orderManagementRepository.countDistinctOrderByOrderDateBetweenAndDevice(startOfDate, endOfDay, device);
            if (userNumber == null) {
                userNumber = 0L;
            }

            Long addUser = Optional.ofNullable(orderManagementRepository.countNewUsersByOrderDateAndJoiningDate(startOfDate, endOfDay, device)).orElse(0L);
            Long repeatBuyers = Optional.ofNullable(orderManagementRepository.countRepeatBuyersByOrderDateAndDevice(startOfDate, endOfDay, device)).orElse(0L);
            Long ordersNumber = Optional.ofNullable(orderManagementRepository.countOrdersByOrderDateBetweenAndDevice(startOfDate, endOfDay, device)).orElse(0L);
            Long oldUserNumber = Optional.ofNullable(orderManagementRepository.countDistinctOrderByOrderDateBeforeAndDevice(startOfDate, device)).orElse(0L);
            BigDecimal countOrderAmount = orderManagementRepository.sumPaymentAmountByOrderDateBetweenAndDevice(startOfDate, endOfDay, device, PAID);
            if (countOrderAmount == null) {
                countOrderAmount = BigDecimal.ZERO;
            }

            double growthRate = (ordersNumber != 0 && oldUserNumber != 0) ? ((double) userNumber / oldUserNumber) * 100 : (oldUserNumber == 0 ? 100.0 : 0.0);
            double repurchaseRate = (userNumber != 0 && repeatBuyers != 0) ? ((double) repeatBuyers / userNumber) * 100 : 0.0;

            UserTotalVO userTotalVO = new UserTotalVO();
            userTotalVO.setRepurchaseRate(repurchaseRate);
            userTotalVO.setGrowthRate(growthRate);
            userTotalVO.setDeviceName(device.getName());
            userTotalVO.setChannelName(channelPartner.getName());
            userTotalVO.setAddress(device.getAddress());
            userTotalVO.setUserNumber(userNumber.intValue());
            userTotalVO.setAddUser(addUser.intValue());
            userTotalVO.setOrdersNumber(ordersNumber.intValue());
            userTotalVO.setCountOrderAmount(countOrderAmount);

            userTotalVOList.add(userTotalVO);
        }

        return new PageImpl<>(userTotalVOList, pageable, devicesLists.getTotalElements());
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void updateSettlementTask() {
        LocalDate now = LocalDate.now();
        SettlementTime settlementTime = settlementTimeRepository.findAll().stream().findFirst().orElse(null);
        if (settlementTime != null && settlementTime.getSettlementDay() != null) {
            Integer settlementDay = settlementTime.getSettlementDay();
            if (now.getDayOfMonth() == settlementDay) {
                log.info("结算定时任务执行");
                updateSettlement();
            }
        }
    }

    @Override
    @Transactional
    public void updateSettlement() {
        log.info("结算任务执行");
        LocalDate now = LocalDate.now();
        SettlementTime settlementTime = settlementTimeRepository.findAll().stream().findFirst().orElse(null);
        LocalDate startDate = calculateStartDate(now, Objects.requireNonNull(settlementTime));
        LocalDate endDate = calculateEndDate(now, settlementTime);
        LocalDateTime startTime = startDate.atStartOfDay();
        LocalDateTime endTime = endDate.atTime(LocalTime.MAX);
        updateDeviceSettlement(startTime, endTime);
        updateChannelPartnerSettlement();
    }

    private LocalDate calculateStartDate(LocalDate now, SettlementTime settlementTime) {
        int startYear = now.getMonthValue() < toMonthNumber(settlementTime.getStartMonth()) ? now.getYear() - 1 : now.getYear();
        return LocalDate.of(startYear, toMonthNumber(settlementTime.getStartMonth()), settlementTime.getStartDay());
    }

    private LocalDate calculateEndDate(LocalDate now, SettlementTime settlementTime) {
        int endYear = now.getMonthValue() < toMonthNumber(settlementTime.getEndMonth()) || (settlementTime.getStartMonth() == settlementTime.getEndMonth() && settlementTime.getStartDay() > settlementTime.getEndDay()) ? now.getYear() - 1 : now.getYear();
        return LocalDate.of(endYear, toMonthNumber(settlementTime.getEndMonth()), settlementTime.getEndDay());
    }

    private void updateDeviceSettlement(LocalDateTime startTime, LocalDateTime endTime) {
        List<DevicesList> devicesLists = devicesListRepository.findAll();
        for (DevicesList device : devicesLists) {
            BigDecimal income = calculateIncome(startTime, endTime, device);
            saveSettlementDetails(device, income);
        }
    }

    private BigDecimal calculateIncome(LocalDateTime startTime, LocalDateTime endTime, DevicesList device) {
        BigDecimal income = orderManagementRepository.sumPaymentAmountByOrderDateBetweenAndDevice(startTime, endTime, device, PAID);
        return income != null ? income : BigDecimal.ZERO;
    }

    private void saveSettlementDetails(DevicesList device, BigDecimal income) {
        SettlementDetails settlementDetails = new SettlementDetails();
        Equipment equipment = new Equipment();
        equipment.setName(device);
        equipment.setRevenue(income);
        equipment.setSettlementMonth(LocalDate.now());
        settlementDetails.setDeviceIncome(income);
        settlementDetails.setSettlementMonth(LocalDate.now());
        settlementDetails.setName(device);
        handleCityPartnerSettlement(device, income, equipment, settlementDetails);

        handleTerminalMerchantsSettlement(device, income, equipment, settlementDetails);

        equipmentRepository.save(equipment);

    }

    private void handleCityPartnerSettlement(DevicesList device, BigDecimal income, Equipment equipment, SettlementDetails settlementDetails) {
        if (device.getCityPartner() != null) {
            Integer settlementRatio2 = device.getCityPartnerRatio();
            BigDecimal dueAmount2 = income.multiply(BigDecimal.valueOf(settlementRatio2).divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.DOWN);
            equipment.setDueAmount2(dueAmount2);
            equipment.setSettlementRatio2(settlementRatio2);
            settlementDetails.setChannelPartner(device.getCityPartner());
            settlementDetails.setSettlementAmount(dueAmount2);
            settlementDetails.setSettlementRatio(settlementRatio2);
            settlementDetailsRepository.save(settlementDetails);
        }
    }

    private void handleTerminalMerchantsSettlement(DevicesList device, BigDecimal income, Equipment equipment, SettlementDetails settlementDetails) {
        if (device.getTerminalMerchants() != null) {
            Integer settlementRatio4 = device.getTerminalMerchantsRatio();
            BigDecimal dueAmount4 = BigDecimal.ZERO;
            if (settlementRatio4 != null) {
                dueAmount4 = income.multiply(BigDecimal.valueOf(settlementRatio4).divide(BigDecimal.valueOf(100), 2, RoundingMode.DOWN));
            }
            equipment.setDueAmount4(dueAmount4);
            equipment.setSettlementRatio4(settlementRatio4);
            settlementDetails.setChannelPartner(device.getTerminalMerchants());
            settlementDetails.setSettlementAmount(dueAmount4);
            settlementDetails.setSettlementRatio(settlementRatio4);
            settlementDetailsRepository.saveAndFlush(settlementDetails);
        }
    }

    private void updateChannelPartnerSettlement() {
        List<ChannelPartner> channelPartners = channelPartnerRepository.findAll();
        for (ChannelPartner channelPartner : channelPartners) {
            ChannelSettlement channelSettlement = new ChannelSettlement();
            channelSettlement.setName(channelPartner);
            channelSettlement.setSettlementMonth(LocalDate.now());
            BigDecimal settlementAmount = settlementDetailsRepository.findSettlementAmountSumByPartnerAndMonth(channelPartner, LocalDate.now());
            channelSettlement.setSettlementAmount(settlementAmount != null ? settlementAmount : BigDecimal.ZERO);
            List<SettlementDetails> relatedSettlementDetails = settlementDetailsRepository.findByChannelPartnerAndSettlementMonth(channelPartner, LocalDate.now());
            channelSettlement.setSettlementDetails(relatedSettlementDetails);
            channelSettlementRepository.save(channelSettlement);
        }
    }

    public int toMonthNumber(Month value) {
        switch (value) {
            case ThisMonth:
                return LocalDate.now().getMonthValue();
            case LastMonth:
                return LocalDate.now().minusMonths(1).getMonthValue();
            case TwoMonthsAgo:
                return LocalDate.now().minusMonths(2).getMonthValue();
        }
        return 0;
    }

}