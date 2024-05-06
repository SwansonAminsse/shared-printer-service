package com.yn.printer.service.modules.operation.vo;

import com.yn.printer.service.modules.enums.DeviceStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@ApiModel(value = "DeviceStatusVO", description = "状态vo")
@AllArgsConstructor
public class DeviceStatusVO {

    @ApiModelProperty(value = "设备状态 " +
            "NOT_ACTIVE(未激活)" +
            "ONLINE(在线)" +
            "OFFLINE(离线)" +
            "RUN(运行)" +
            "ABNORMAL(异常)" +
            "STOP(停用)")
    private DeviceStatus status;
    @ApiModelProperty(value = "异常原因")
    private String abnormalReason;

}