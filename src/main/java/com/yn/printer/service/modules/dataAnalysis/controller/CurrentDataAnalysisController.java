package com.yn.printer.service.modules.dataAnalysis.controller;

import com.yn.printer.service.modules.dataAnalysis.service.CurrentDataAnalysisService;
import com.yn.printer.service.modules.dataAnalysis.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "CurrentDataAnalysisController", tags = "用户端-实时数据分析")
@RestController
@RequestMapping("/currentdataanalysis")
public class CurrentDataAnalysisController {
    @Autowired
    private CurrentDataAnalysisService currentDataAnalysisService;

    @GetMapping("/totaldata")
    @ApiOperation(value = "总数据")
    public TotalDevicesVO totalData() {
        return currentDataAnalysisService.totalData();
    }

    @GetMapping("/todayorders")
    @ApiOperation(value = "实时订单详情")
    public Page<OrderVO> getTodayOrders(Pageable pageable) {
        return currentDataAnalysisService.getTodayOrders(pageable);
    }

    @GetMapping("/getusertodayanalysis")
    @ApiOperation(value = "实时用户数据")
    public UserAnalysisVO getUserTodayAnalysis() {
        return currentDataAnalysisService.getUserTodayAnalysis();
    }

    @GetMapping("/getordertotal")
    @ApiOperation(value = "实时订单数据")
    public OrderTotalVO getOrderTotal() {
        return currentDataAnalysisService.getOrderTotal();
    }

    @GetMapping("/getncometotal")
    @ApiOperation(value = "实时收入数据")
    public IncometotalVO getIncomeTotal() {
        return currentDataAnalysisService.getIncomeTotal();
    }
}
