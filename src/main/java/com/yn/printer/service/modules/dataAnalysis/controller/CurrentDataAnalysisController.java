package com.yn.printer.service.modules.dataAnalysis.controller;

import com.yn.printer.service.modules.dataAnalysis.service.CurrentDataAnalysisService;
import com.yn.printer.service.modules.dataAnalysis.vo.*;
import com.yn.printer.service.modules.operation.entity.DevicesList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

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
    public Page<OrderVO> getTodayOrders(@RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "size", defaultValue = "10") int size) {
        return currentDataAnalysisService.getTodayOrders(PageRequest.of(page, size));
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

    @GetMapping("/getAllChannelPartner")
    @ApiOperation(value = "获取全部渠道商")
    public Page<ChannelPartnerInfo> getAllChannelPartner(@RequestParam(value = "page", defaultValue = "0") int page,
                                                         @RequestParam(value = "size", defaultValue = "10") int size) {
        return currentDataAnalysisService.getAllChannelPartner(PageRequest.of(page, size));
    }

    @GetMapping("/getDeviceByChannelPartner")
    @ApiOperation(value = "设备统计")
    public List<DeviceStatisticsVO> getDeviceByChannelPartner(Long channelPartnerId, String dateTime) {
        return currentDataAnalysisService.getDeviceByChannelPartner(channelPartnerId, dateTime);
    }

    @GetMapping("/getUserByChannelPartnerAndDateTime")
    @ApiOperation(value = "用户统计")
    public UserStatisticsVO getUserByChannelPartnerAndDateTime(Long channelPartnerId, String dateTime) {
        return currentDataAnalysisService.getUserByChannelPartnerAndDateTime(channelPartnerId, dateTime);
    }

    @GetMapping("/getTaskByChannelPartner")
    @ApiOperation(value = "运维统计")
    public Boolean getTaskByChannelPartner(Long channelPartnerId, String dateTime) {
        return false;
    }

    @GetMapping("/getDateBySelect")
    @ApiOperation(value = "所在日期")
    public List<LocalDate> getDateBySelect(String dateTime) {
        return currentDataAnalysisService.getDateBySelect(dateTime);
    }

    @GetMapping("/getOrderPrintType")
    @ApiOperation(value = "订单类型")
    public OrderStatisticsVO getOrderPrintType(Long channelPartnerId, String dateTime) {
        return currentDataAnalysisService.getOrderPrintType(channelPartnerId, dateTime);
    }

    @GetMapping("/getOrderAmountByOrderPrintType")
    @ApiOperation(value = "类型金额")
    public OrderAmountStatisticsVO getOrderAmountByOrderPrintType(Long channelPartnerId, String dateTime) {
        return currentDataAnalysisService.getOrderAmountByOrderPrintType(channelPartnerId, dateTime);
    }

    @GetMapping("/getSingleOrderAmount")
    @ApiOperation(value = "单笔订单金额")
    public SingleOrderAmountStatisticsVO getSingleOrderAmount(Long channelPartnerId, String dateTime) {
        return currentDataAnalysisService.getSingleOrderAmount(channelPartnerId, dateTime);

    }

    @GetMapping("/getOrderIncomeRate")
    @ApiOperation(value = "各类收入占比")
    public OrderIncomeRateVo getOrderIncomeRate(Long channelPartnerId, String dateTime) {
        return currentDataAnalysisService.getOrderIncomeRate(channelPartnerId, dateTime);
    }

    @GetMapping("/getDeviceRank")
    @ApiOperation(value = "站点排行")
    public List<DeviceRankVO> getDeviceRank(Long channelPartnerId, String dateTime) {
        return currentDataAnalysisService.getDeviceRank(channelPartnerId, dateTime);
    }

    @GetMapping("/getDateRange")
    @ApiOperation(value = "日期范围选择")
    public List<String> getDateRange() {
        return currentDataAnalysisService.getDateRange();
    }


}
