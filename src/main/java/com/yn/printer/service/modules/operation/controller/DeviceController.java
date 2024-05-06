package com.yn.printer.service.modules.operation.controller;

import com.yn.printer.service.common.vo.ResponseVO;
import com.yn.printer.service.modules.common.mqtt.dto.PrintTaskPush;
import com.yn.printer.service.modules.meta.entity.DeviceInterface;
import com.yn.printer.service.modules.operation.dto.PreprintTask;
import com.yn.printer.service.modules.operation.dto.SubmitPrintingTaskDto;
import com.yn.printer.service.modules.operation.service.IDeviceService;
import com.yn.printer.service.modules.operation.vo.DeviceStatusVO;
import com.yn.printer.service.modules.operation.vo.DeviceVo;
import com.yn.printer.service.modules.operation.vo.DevicesListVO;
import com.yn.printer.service.modules.operation.vo.GoodsVo;
import com.yn.printer.service.modules.operation.vo.DeviceInterfaceVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Validated
@Api(value = "MemberController", tags = "公共端-设备")
@RestController
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    IDeviceService deviceService;

    @ApiOperation(value = "设备-添加新设备")
    @GetMapping("/addNewDevice")
    public ResponseVO<String> addNewDevice() {
        return ResponseVO.newInstance(deviceService.addNewDevice());
    }

    @ApiOperation(value = "设备-根据编码获取设备信息")
    @GetMapping("/getDeviceByCode")
    public DeviceVo getDeviceByCode(@RequestParam @ApiParam(value = "设备编码", required = true) String code) {
        return deviceService.findByCode(code);
    }
    @ApiOperation(value = "设备-根据编码获取接口信息")
    @GetMapping("/getDeviceInterface")
    public List<DeviceInterfaceVO> getDeviceInterface(@RequestParam @ApiParam(value = "设备编码", required = true) String code) {
        return deviceService.getPrintInterface(code);
    }

    @ApiOperation(value = "设备-获取打印价格")
    @GetMapping("/getPrintPrice")
    public BigDecimal getPrintPrice(@RequestParam Long deviceId, PreprintTask task) {
        return deviceService.getPrintPrice(deviceId, task);
    }
    @ApiOperation(value = "设备-获取打印机名称")
    @PostMapping("/getPrint")
    public List<String> getPrint(@RequestParam String deviceCode,String name) {
        return deviceService.getPrint(deviceCode,name);
    }

    @ApiOperation(value = "设备-获取打印价格列表")
    @GetMapping("/getPrintPriceList")
    public List<GoodsVo> getPrintPriceList(@RequestParam Long deviceId) {
        return deviceService.getPrintPriceList(deviceId);
    }

    @ApiOperation(value = "设备-获取异常原因")
    @GetMapping("/getAbnormalReason")
    public DeviceStatusVO getDeviceStatusVO(@RequestParam String code) {
        return deviceService.getDeviceStatusVO(code);
    }

    @ApiOperation(value = "设备-提交打印任务")
    @PostMapping("/submitPrintingTask")
    public Long submitPrintingTask(@RequestBody SubmitPrintingTaskDto dto) {
        return deviceService.submitPrintingTask(dto, null);
    }

    @ApiOperation(value = "订单-获取打印任务")
    @GetMapping("/getPrintTask")
    public PrintTaskPush getPrintTask(@RequestParam Long orderId) {
        return deviceService.getPrintTask(orderId);
    }

    @GetMapping("/userDevicesList")
    @ApiOperation(value = "周围设备列表（用户）", notes = "运营用户登录后通过经纬度获取周围的设备", response = Page.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码（默认为0）", defaultValue = "0", dataType = "java.lang.Integer", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页条数（默认为10）", defaultValue = "10", dataType = "java.lang.Integer", paramType = "query"),
            @ApiImplicitParam(name = "latitude", value = "纬度", required = true, dataType = "java.lang.Double", paramType = "query"),
            @ApiImplicitParam(name = "longitude", value = "经度", required = true, dataType = "java.lang.Double", paramType = "query")
    })
    public Page<DevicesListVO> getUserDevicesList(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude) {
        return deviceService.findDevicesUser(latitude, longitude, PageRequest.of(page, size));
    }
}
