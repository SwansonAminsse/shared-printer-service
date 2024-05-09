package com.yn.printer.service.modules.dataAnalysis.controller;

import com.yn.printer.service.modules.dataAnalysis.service.DataAnalysisService;
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
    private DataAnalysisService dataAnalysisService;

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

//    @GetMapping("/ChannelSelect")
//    @ApiOperation(value = "渠道商选择器")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "page", value = "页码（默认为0）", defaultValue = "0", dataType = "java.lang.Integer", paramType = "query"),
//            @ApiImplicitParam(name = "size", value = "每页条数（默认为10）", defaultValue = "10", dataType = "java.lang.Integer", paramType = "query"),
//           })
//    public Page<ChannelSelectVO> getChannelSelectVO(@RequestParam(value = "page", defaultValue = "0") int page,
//                                                    @RequestParam(value = "size", defaultValue = "10") int size,) {
//        return dataAnalysisService.getChannelSelectVO(PageRequest.of(page, size));
//    }


}