package com.yn.printer.service.modules.common.mqtt.dto;

import com.yn.printer.service.modules.common.mqtt.MessageType;
import com.yn.printer.service.modules.member.vo.MemberLoginVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "MemberLoginPush", description = "21寸屏用户登录请求实例")
public class MemberLogoutPush extends BasePush {

    @ApiModelProperty(value = "命令")
    final MessageType cmd = MessageType.LOGOUT;

}
