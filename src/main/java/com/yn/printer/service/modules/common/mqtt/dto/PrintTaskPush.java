package com.yn.printer.service.modules.common.mqtt.dto;

import com.yn.printer.service.modules.common.mqtt.MessageType;
import com.yn.printer.service.modules.meta.enums.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author : Jonas Chan
 * @since : 2024/1/3 17:09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "PrintTaskPush", description = "打印任务推送实例")
public class PrintTaskPush extends BasePush {

  @ApiModelProperty(value = "命令")
  final MessageType cmd = MessageType.PRINT;

  @ApiModelProperty(value = "任务号")
  private String code;

  @ApiModelProperty(value = "打印类型")
  private PrintType printType;

  @ApiModelProperty(value = "打印文件类型")
  private PrintFileType printFileType;

  @ApiModelProperty(value = "打印文件")
  private Long printFileId;

  @ApiModelProperty(value = "打印文件")
  private String printFileName;

  @ApiModelProperty(value = "打印颜色")
  private PrintColor printingColor;

  @ApiModelProperty(value = "打印面数")
  private PrintFaces printingFaces;

  @ApiModelProperty(value = "打印方向")
  private PrintDirection printDirection;

  @ApiModelProperty(value = "打印份数")
  private Integer copies = 0;

  @ApiModelProperty(value = "文件页数")
  private Integer pageSize = 1;

  @ApiModelProperty(value = "A4纸打印机名称")
  private String a4PrinterName = "EPSON WF-C5890 Series2";

  @ApiModelProperty(value = "相纸打印机名称")
  private String photoPrinterName = "EPSON WF-C5890 Series3";
}
