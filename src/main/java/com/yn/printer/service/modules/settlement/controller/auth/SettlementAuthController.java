package com.yn.printer.service.modules.settlement.controller.auth;

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
@RequestMapping("/api/settlement_details")
@Api(tags = "会员端-运营", value = "成本收益分析")
public class SettlementAuthController {
    @Autowired
    ISettlementDetailsService settlementDetailsService;
    @Autowired
    ITaskListService taskListService;

    @GetMapping("/total")

    @ApiOperation(value = "统计设备收入", notes = "统计总收入平均结算比例和总结算金额")
    public IncomeSumVO getDetailsTotalIncome(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ) {
        return settlementDetailsService.getDeviceIncomeSum(startDate, endDate);
    }

    @GetMapping("/income")

    @ApiOperation(value = "设备收入列表", notes = "成本默认为0")
    public Page<IncomeVO> getDetailsIncome(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return settlementDetailsService.getDeviceIncome(startDate, endDate, PageRequest.of(page, size));
    }

    @PostMapping("/adduser")

    @ApiOperation(value = "添加内部用户", notes = "将手机号标记为内部账号")
    public Boolean addUser(
            @RequestParam() String userName,
            @RequestParam() String userPhone
    ) {
        return settlementDetailsService.addUser(userName, userPhone);
    }

    @GetMapping("/statistics")
    @ApiOperation(value = "统计运维任务(运营)", notes = "页面上的已读未读已处理未处理数量")
    public TaskStatisticsVO getStatistics(@RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                          @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ) {
        return taskListService.getOperationStatistics(startDate, endDate);
    }

    @GetMapping("/userTotal")

    @ApiOperation(value = "用户分析、设备分析", notes = "统计用户交易记录")
    public Page
            <UserTotalVO> getUserTotal(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return settlementDetailsService.userTotal(startDate, endDate, PageRequest.of(page, size));
    }

}