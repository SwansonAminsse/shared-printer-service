package com.yn.printer.service.modules.dataAnalysis.controller;

import com.yn.printer.service.modules.dataAnalysis.service.CurrentDataAnalysisService;
import com.yn.printer.service.modules.dataAnalysis.vo.*;
import com.yn.printer.service.modules.operation.vo.ChannelSelectVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "CurrentDataAnalysisController", tags = "PC后台端-实时数据分析")
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
    public Page<OrderVO> getTodayOrders() {
        return currentDataAnalysisService.getTodayOrders(PageRequest.of(0, 10));
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

    @GetMapping("/ChannelSelect")
    @ApiOperation(value = "渠道商选择器")
    @ApiImplicitParams({@ApiImplicitParam(name = "page", value = "页码（默认为0）", defaultValue = "0", dataType = "java.lang.Integer", paramType = "query"), @ApiImplicitParam(name = "size", value = "每页条数（默认为10）", defaultValue = "10", dataType = "java.lang.Integer", paramType = "query"),})
    public Page<ChannelSelectVO> getChannelSelectVO(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10") int size) {
        return null;
    }
}
