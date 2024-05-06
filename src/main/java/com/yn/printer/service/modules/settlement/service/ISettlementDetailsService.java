package com.yn.printer.service.modules.settlement.service;

import com.yn.printer.service.modules.settlement.vo.IncomeSumVO;
import com.yn.printer.service.modules.settlement.vo.IncomeVO;
import com.yn.printer.service.modules.settlement.vo.UserTotalVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ISettlementDetailsService {
     IncomeSumVO getDeviceIncomeSum(LocalDate startDate, LocalDate endDate);

     Page<IncomeVO> getDeviceIncome(LocalDate startDate, LocalDate endDate, Pageable pageable);

    Boolean addUser(String userName, String userPhone);
    Page<UserTotalVO> userTotal(LocalDate startDate, LocalDate endDate, Pageable pageable);

    void updateSettlement();
}
