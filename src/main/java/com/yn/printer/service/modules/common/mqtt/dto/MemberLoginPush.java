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
public class MemberLoginPush extends BasePush {

    @ApiModelProperty(value = "命令")
    final MessageType cmd = MessageType.LOGIN;

    @ApiModelProperty(value = "会话token")
    private String token;

    @ApiModelProperty(value = "会员名称")
    private String name;

    @ApiModelProperty(value = "电话号")
    private String phoneNumber;

    @ApiModelProperty(value = "入会日期")
    private LocalDate joiningDate;

    @ApiModelProperty(value = "累计积分")
    private Integer accumulatedPoints = 0;

    @ApiModelProperty(value = "已消耗积分")
    private Integer consumedPoints = 0;

    @ApiModelProperty(value = "累计消费金额")
    private BigDecimal accumulatedAmount = BigDecimal.ZERO;

    @ApiModelProperty(value = "账户余额")
    private BigDecimal accountBalance = BigDecimal.ZERO;

    @ApiModelProperty(value = "是否运维人员")
    private Boolean operation = false;

    @ApiModelProperty(value = "是否运营人员")
    private Boolean business = false;

    @ApiModelProperty(value = "头像路径")
    private String imageUrl;

}
