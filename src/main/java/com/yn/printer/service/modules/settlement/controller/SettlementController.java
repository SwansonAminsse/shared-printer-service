package com.yn.printer.service.modules.settlement.controller;

import com.yn.printer.service.modules.operation.service.ITaskListService;
import com.yn.printer.service.modules.operation.vo.TaskStatisticsVO;
import com.yn.printer.service.modules.settlement.service.ISettlementDetailsService;
import com.yn.printer.service.modules.settlement.vo.IncomeSumVO;
import com.yn.printer.service.modules.settlement.vo.IncomeVO;
import com.yn.printer.service.modules.settlement.vo.UserTotalVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
@RestController
@RequestMapping("/api/settlement")
@Api(tags = "结算", value = "手动结算")
public class SettlementController {
    @Autowired
    ISettlementDetailsService settlementDetailsService;
    @GetMapping("/update")

     public Boolean updateSettlement() {
        settlementDetailsService.updateSettlement();
        return true;
    }
}