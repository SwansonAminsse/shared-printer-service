package com.yn.printer.service.modules.dataAnalysis.controller;

import com.yn.printer.service.modules.dataAnalysis.service.IDataAnalysisService;
import com.yn.printer.service.modules.dataAnalysis.vo.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/dataAnalysis")
public class DataAnalysisController {
    @Autowired
    private IDataAnalysisService dataAnalysisService;

    @GetMapping("/channel")
    @ApiOperation(value = "渠道")
    public TotalChannelVO getTotalChannelVO() {
        return dataAnalysisService.getTotalChannel();
    }

    @GetMapping("/income")
    @ApiOperation(value = "成本分析卡片")
    public IncomeSumVO getOperationDevicesList(@RequestParam Boolean time) {
        return dataAnalysisService.totalIncome(time);
    }

    @GetMapping("/user")
    @ApiOperation(value = "用户分析卡片")
    public UserTotalVO getUserTotal(@RequestParam Boolean time) {
        return dataAnalysisService.getUserTotal(time);
    }

    @GetMapping("/devices")
    @ApiOperation(value = "设备运营分析卡片")
    public DevicesDataVO getDevicesData(@RequestParam Boolean time) {
        return dataAnalysisService.getDevicesData(time);
    }

    @GetMapping("/Volume")
    @ApiOperation(value = "用户人次和消费额曲线")
    public UsersAndVolumeOfWeekVO getUsersAndVolumeOfWeekVO() {
        return dataAnalysisService.getUsersAndVolumeOfWeekVO();
    }

    @GetMapping("/GrossProfit")
    @ApiOperation(value = "毛利曲线")
    public List<BigDecimal> getGrossProfit() {
        return dataAnalysisService.getGrossProfit();
    }




}