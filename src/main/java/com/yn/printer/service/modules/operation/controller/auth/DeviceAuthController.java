package com.yn.printer.service.modules.operation.controller.auth;

import com.yn.printer.service.interceptor.AuditInterceptor;
import com.yn.printer.service.modules.operation.dto.SubmitPrintingTaskDto;
import com.yn.printer.service.modules.operation.service.IDeviceService;
import com.yn.printer.service.modules.operation.vo.DevicesListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@Api(value = "MemberController", tags = "用户端-设备")
@RestController
@RequestMapping("/device/auth")
public class DeviceAuthController {

    @Autowired
    IDeviceService deviceService;

    @ApiOperation(value = "设备-提交打印任务")
    @PostMapping("/submitPrintingTask")
    public Long submitPrintingTask(@RequestBody SubmitPrintingTaskDto dto) {
        return deviceService.submitPrintingTask(dto, AuditInterceptor.CURRENT_MEMBER.get());
    }

    @ApiOperation(value = "设备-绑定用户")
    @PostMapping("/bindUser")
    public Boolean bindUser(@RequestParam String code) {
        return deviceService.bindUser(code);
    }

    @ApiOperation(value = "设备-解除绑定用户")
    @PostMapping("/disBindUser")
    public Boolean disBindUser(@RequestParam String code) {
        return deviceService.disBindUser(code);
    }

    @GetMapping("/list")
    @ApiOperation(value = "周围设备列表（运维）", notes = "用户登录后通过经纬度获取周围的设备", response = Page.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码（默认为0）", defaultValue = "0", dataType = "java.lang.Integer", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页条数（默认为10）", defaultValue = "10", dataType = "java.lang.Integer", paramType = "query"),
            @ApiImplicitParam(name = "latitude", value = "纬度", required = true, dataType = "java.lang.Double", paramType = "query"),
            @ApiImplicitParam(name = "longitude", value = "经度", required = true, dataType = "java.lang.Double", paramType = "query")
    })
    public Page<DevicesListVO> getDevicesList(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude) {
        return deviceService.findDevicesByPhoneNumberAndLocation(latitude, longitude, PageRequest.of(page, size));
    }

    @GetMapping("/operationDevicesList")
    @ApiOperation(value = "周围设备列表（运营）", notes = "运营用户登录后通过经纬度获取周围的设备", response = Page.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码（默认为0）", defaultValue = "0", dataType = "java.lang.Integer", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页条数（默认为10）", defaultValue = "10", dataType = "java.lang.Integer", paramType = "query"),
            @ApiImplicitParam(name = "latitude", value = "纬度", required = true, dataType = "java.lang.Double", paramType = "query"),
            @ApiImplicitParam(name = "longitude", value = "经度", required = true, dataType = "java.lang.Double", paramType = "query")
    })
    public Page<DevicesListVO> getOperationDevicesList(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude) {
        return deviceService.findDevicesAndLocation(latitude, longitude, PageRequest.of(page, size));
    }



}
