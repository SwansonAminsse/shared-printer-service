package com.yn.printer.service.modules.operation.vo;

import com.yn.printer.service.modules.enums.ChannelType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ChannelSelectVO {

    @ApiModelProperty(value = "渠道商id")
    private Long id;

    @ApiModelProperty(value = "渠道商类型")
    private ChannelType channelType;

    @ApiModelProperty(value = "渠道商名称")
    private String name;

}