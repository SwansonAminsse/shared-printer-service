package com.yn.printer.service.modules.dataAnalysis.controller;

import com.yn.printer.service.modules.dataAnalysis.enums.TimeSelect;
import com.yn.printer.service.modules.dataAnalysis.service.ICurrentDataAnalysisService;
import com.yn.printer.service.modules.dataAnalysis.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public ChannelDataVO realTimeData(Long channelPartnerId, TimeSelect dateTime) {
        ChannelDataVO channelDataVO = new ChannelDataVO();
        TaskByChannelVO taskByChannelVO = new TaskByChannelVO();
        channelDataVO.setDeviceStatistics(currentDataAnalysisService.getDeviceByChannelPartner(channelPartnerId, dateTime));
        channelDataVO.setUserStatistics(currentDataAnalysisService.getUserByChannelPartnerAndDateTime(channelPartnerId, dateTime));
        channelDataVO.setTaskByChannel(taskByChannelVO);
        return channelDataVO;
    }

    @GetMapping("/channelOrder")
    @ApiOperation(value = "依据渠道和时间统计订单数据")
    public ChannelOrderVO channelOrder(Long channelPartnerId, TimeSelect dateTime) {
        ChannelOrderVO channelOrderVO = new ChannelOrderVO();
        channelOrderVO.setOrderStatistics(currentDataAnalysisService.getOrderPrintType(channelPartnerId, dateTime));
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
