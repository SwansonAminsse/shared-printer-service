package com.yn.printer.service.modules.common.mqtt.dto;

import com.yn.printer.service.modules.common.mqtt.MessageType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "AdvertisementPush", description = "广告更新推送")
public class AdvertisementPush extends BasePush{
    @ApiModelProperty(value = "命令")
    final MessageType cmd = MessageType.CHANGE;
}


