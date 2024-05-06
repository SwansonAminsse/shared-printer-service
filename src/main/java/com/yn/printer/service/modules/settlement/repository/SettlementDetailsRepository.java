package com.yn.printer.service.modules.settlement.repository;

import com.yn.printer.service.modules.channel.entity.ChannelPartner;
import com.yn.printer.service.modules.channel.entity.ChannelUser;
import com.yn.printer.service.modules.member.entity.Member;
import com.yn.printer.service.modules.operation.vo.DevicesListVO;
import com.yn.printer.service.modules.settlement.vo.IncomeSumVO;
import com.yn.printer.service.modules.settlement.vo.IncomeVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.yn.printer.service.modules.settlement.entity.SettlementDetails;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public  interface SettlementDetailsRepository extends JpaRepository<SettlementDetails, Long>, JpaSpecificationExecutor<SettlementDetails> {
    List<SettlementDetails> findBySettlementMonthBetween(LocalDate startDate, LocalDate endDate);
    @Query("SELECT new com.yn.printer.service.modules.settlement.vo.IncomeSumVO(SUM(s.deviceIncome), AVG(s.settlementRatio), SUM(s.settlementAmount)) FROM SettlementDetails s WHERE  s.settlementMonth BETWEEN :startDate AND :endDate AND :channelPartner  IN s.channelPartner")
    IncomeSumVO findTotalIncomeAndAverageSettlementRatio(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,@Param("channelPartner") ChannelPartner channelPartner);

    @Query("SELECT new com.yn.printer.service.modules.settlement.vo.IncomeVO(s.name.name,s.channelPartner.name,s.deviceIncome,s.settlementRatio,s.settlementAmount,s.alreadyPaid,s.name.rentalFees) FROM SettlementDetails s WHERE  s.settlementMonth BETWEEN :startDate AND :endDate AND :channelPartner  IN s.channelPartner")
    Page<IncomeVO> findIncomeList(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("channelPartner") ChannelPartner channelPartner,
                                  Pageable pageable);

    default Page<IncomeVO> findIncomeListWithCost(
            LocalDate startDate, LocalDate endDate, ChannelPartner channelPartner, Pageable pageable) {
        Page<IncomeVO> incomeListPage = findIncomeList(startDate, endDate, channelPartner, pageable);
        List<IncomeVO> incomeList = incomeListPage.getContent().stream()
                .map(IncomeVO::cost)
                .collect(Collectors.toList());
        return new PageImpl<>(incomeList, pageable, incomeListPage.getTotalElements());

    }
    @Query("SELECT SUM(sd.settlementAmount) FROM SettlementDetails sd WHERE sd.channelPartner = :partner AND sd.settlementMonth = :month")
    BigDecimal findSettlementAmountSumByPartnerAndMonth(@Param("partner") ChannelPartner partner, @Param("month") LocalDate month);
    @Query("SELECT sd FROM SettlementDetails sd WHERE sd.channelPartner = :partner AND sd.settlementMonth = :month")
    List<SettlementDetails> findByChannelPartnerAndSettlementMonth(@Param("partner") ChannelPartner partner, @Param("month") LocalDate month);
}