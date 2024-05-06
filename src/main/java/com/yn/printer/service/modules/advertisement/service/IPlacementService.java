package com.yn.printer.service.modules.advertisement.service;

import com.yn.printer.service.modules.advertisement.enums.Screen;
import com.yn.printer.service.modules.advertisement.vo.PlacementVO;
import com.yn.printer.service.modules.common.vo.MetaFileVo;
import com.yn.printer.service.modules.operation.entity.DevicesList;

import java.util.List;

public interface IPlacementService {

    List<PlacementVO> getAddress(Long deviceId, Screen screen);

    void changeAdvertisement(String code);

}