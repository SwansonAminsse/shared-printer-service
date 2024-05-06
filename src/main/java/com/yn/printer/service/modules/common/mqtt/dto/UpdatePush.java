package com.yn.printer.service.modules.common.mqtt.dto;
//
//import com.yn.printer.service.modules.common.mqtt.MessageType;
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//
//
//@Data
//@EqualsAndHashCode(callSuper = true)
//@ApiModel(value = "UpdatePush", description = "更新推送实例")
//public class UpdatePush extends BasePush{
//    @ApiModelProperty(value = "命令")
//    final MessageType cmd = MessageType.UPDATE;
//    //下载地址
//    private String url;
//    //更新日志
//    private String log;
//}