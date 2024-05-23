package com.yn.printer.service.modules.dataAnalysis.controller;

import com.yn.printer.service.modules.dataAnalysis.enums.TimeSelect;
import com.yn.printer.service.modules.dataAnalysis.service.ICurrentDataAnalysisService;
import com.yn.printer.service.modules.dataAnalysis.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

@Api(value = "CurrentDataAnalysisController", tags = "PC后台端-实时数据分析")
@RestController
@RequestMapping("/cryptanalysis")
public class CurrentDataAnalysisController {
    @Resource
    private ICurrentDataAnalysisService currentDataAnalysisService;

    @GetMapping("/realTimeData")
    @ApiOperation(value = "当前实时数据（不区分渠道商和时间）")
    public RealTimeDataVO realTimeData() {
        RealTimeDataVO realTimeDataVO = new RealTimeDataVO();
        realTimeDataVO.setTotalDevices(currentDataAnalysisService.totalData());
        realTimeDataVO.setUserAnalysis(currentDataAnalysisService.getUserTodayAnalysis());
        realTimeDataVO.setOrderTotal(currentDataAnalysisService.getOrderTotal());
        realTimeDataVO.setIncomeTotal(currentDataAnalysisService.getIncomeTotal());
        return realTimeDataVO;
    }

    @GetMapping("/channelData")
    @ApiOperation(value = "依据渠道和时间统计设备、用户和运维数据")
    public ChannelDataVO realTimeData(
            @RequestParam(value = "channelPartnerId", required = false) Long channelPartnerId,
            @RequestParam(value = "dateTime", required = false) TimeSelect dateTime,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        if (dateTime == null && startDate == null && endDate == null) {
            List<LocalDate> todayRange = currentDataAnalysisService.getDateBySelect(TimeSelect.TODAY);
            startDate = todayRange.get(0);
            endDate = todayRange.get(1);
        } else if (dateTime != null) {
            List<LocalDate> dateRange = currentDataAnalysisService.getDateBySelect(dateTime);
            startDate = dateRange.get(0);
            endDate = dateRange.get(1);
        } else if (startDate == null && endDate != null) {
            startDate = endDate;
        } else if (startDate != null && endDate == null) {
            endDate = startDate;
        }
        ChannelDataVO channelDataVO = new ChannelDataVO();
        TaskByChannelVO taskByChannelVO = new TaskByChannelVO();

        channelDataVO.setDeviceStatistics(currentDataAnalysisService.getDeviceByChannelPartner(channelPartnerId));
        channelDataVO.setUserStatistics(currentDataAnalysisService.getUserByChannelPartnerAndDateTime(channelPartnerId, startDate, endDate));
        channelDataVO.setTaskByChannel(taskByChannelVO);

        return channelDataVO;
    }

    @GetMapping("/channelOrder")
    @ApiOperation(value = "依据渠道和时间统计订单数据")
    public ChannelOrderVO channelOrder(
            @RequestParam(value = "channelPartnerId", required = false) Long channelPartnerId,
            @RequestParam(value = "dateTime", required = false) TimeSelect dateTime,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        if (dateTime == null && startDate == null && endDate == null) {
            List<LocalDate> todayRange = currentDataAnalysisService.getDateBySelect(TimeSelect.TODAY);
            startDate = todayRange.get(0);
            endDate = todayRange.get(1);
        } else if (dateTime != null) {
            List<LocalDate> dateRange = currentDataAnalysisService.getDateBySelect(dateTime);
            startDate = dateRange.get(0);
            endDate = dateRange.get(1);
        } else if (startDate == null || endDate == null) {
            endDate = (endDate != null) ? endDate : startDate;
            startDate = (startDate != null) ? startDate : endDate;
        }
        ChannelOrderVO channelOrderVO = new ChannelOrderVO();
        channelOrderVO.setOrderStatistics(currentDataAnalysisService.getOrderPrintType(channelPartnerId, startDate, endDate));
        channelOrderVO.setSingleOrderAmountStatistics(currentDataAnalysisService.getSingleOrderAmount(channelPartnerId, dateTime));
        channelOrderVO.setOrderAmountStatistics(currentDataAnalysisService.getOrderAmountByOrderPrintType(channelPartnerId, dateTime));
        channelOrderVO.setOrderIncomeRate(currentDataAnalysisService.getOrderIncomeRate(channelPartnerId, dateTime));
        channelOrderVO.setDeviceRankList(currentDataAnalysisService.getDeviceRank(channelPartnerId, dateTime));
        return channelOrderVO;
    }

    @GetMapping("/today-orders")
    @ApiOperation(value = "实时订单详情")
    public Page<OrderVO> getTodayOrders(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10") int size) {
        return currentDataAnalysisService.getTodayOrders(PageRequest.of(page, size));
    }

    @GetMapping("/getDateBySelect")
    @ApiOperation(value = "所在日期")
    public List<LocalDate> getDateBySelect(TimeSelect dateTime) {
        return currentDataAnalysisService.getDateBySelect(dateTime);
    }
    @GetMapping("/getAllChannelPartner")
    @ApiOperation(value = "获取全部渠道商")
    public Page<ChannelPartnerInfo> getAllChannelPartner(@RequestParam(value = "page", defaultValue = "0") int page,
                                                         @RequestParam(value = "size", defaultValue = "10") int size) {
        return currentDataAnalysisService.getAllChannelPartner(PageRequest.of(page, size));
    }

}
