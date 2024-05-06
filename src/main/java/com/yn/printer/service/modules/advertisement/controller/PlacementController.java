package com.yn.printer.service.modules.advertisement.controller;

import com.yn.printer.service.modules.advertisement.enums.Screen;
import com.yn.printer.service.modules.advertisement.service.IPlacementService;
import com.yn.printer.service.modules.advertisement.vo.PlacementVO;
import com.yn.printer.service.modules.common.vo.MetaFileVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@Api(value = "PlacementController", tags = "设备端-广告投放")
@RestController
@RequestMapping("/placement")
public class PlacementController {

    @Autowired
    private IPlacementService placementService;

    @GetMapping("/getAdvertisement")
    @ApiOperation(value = "获取广告链接", notes = "接收设备id和屏幕位置返回广告内容链接")
    public List<PlacementVO> getAdvertisementAddress(
            @RequestParam(value = "deviceId") long deviceId,
            @RequestParam(value = "screen") Screen screen
    ) {
        return placementService.getAddress(deviceId, screen);
    }
    @PostMapping("/changeNotice")
    @ApiOperation(value = "广告更新通知", notes = "pc端更新广告活动向前端通知更新")
    public void changeNotice(
            @RequestParam String code
    ) {
        placementService.changeAdvertisement(code);
    }

}