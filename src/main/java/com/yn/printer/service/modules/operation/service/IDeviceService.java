package com.yn.printer.service.modules.operation.service;

import com.yn.printer.service.modules.common.mqtt.dto.PrintTaskPush;
import com.yn.printer.service.modules.member.entity.Member;
import com.yn.printer.service.modules.meta.entity.DeviceInterface;
import com.yn.printer.service.modules.operation.dto.PreprintTask;
import com.yn.printer.service.modules.operation.dto.SubmitPrintingTaskDto;
import com.yn.printer.service.modules.operation.entity.DevicesList;
import com.yn.printer.service.modules.operation.vo.*;
import com.yn.printer.service.modules.orders.entity.OrderManagement;
import com.yn.printer.service.modules.orders.entity.PrintTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface IDeviceService {

    String addNewDevice();

    DeviceVo findByCode(String code);

    BigDecimal getPrintPrice(Long deviceId, PreprintTask task);

    Boolean bindUser(String code);

    Boolean disBindUser(String code);

    Boolean disBindUser(DevicesList device);

    Long submitPrintingTask(SubmitPrintingTaskDto dto, Member member);

    void executePrintingTask(OrderManagement order);

    boolean printOrderPush(OrderManagement order);

    boolean printTaskPush(PrintTask printTask);

    List<GoodsVo> getPrintPriceList(Long deviceId);

    PrintTaskPush getPrintTask(Long orderId);

    Page<DevicesListVO> findDevicesByPhoneNumberAndLocation(double latitude, double longitude, Pageable pageable);

    Page<DevicesListVO> findDevicesAndLocation(double latitude, double longitude, Pageable pageable);

    Page<DevicesListVO> findDevicesUser(double latitude, double longitude, Pageable pageable);

    void mqttPush(DevicesList device, String message);

    void mqttCallback(String deviceCode, String message);

    void saveArea(Long deviceId, double latitude, double longitude);

    List<String> getPrint(String deviceCode,String name);

    DeviceStatusVO getDeviceStatusVO(String code);

    List<DeviceInterfaceVO> getPrintInterface(String deviceCode);
}
