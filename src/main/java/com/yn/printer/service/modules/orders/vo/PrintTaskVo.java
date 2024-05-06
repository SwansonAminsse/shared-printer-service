package com.yn.printer.service.modules.orders.vo;

import com.yn.printer.service.common.entity.AuditableModel;
import com.yn.printer.service.modules.meta.entity.MetaFile;
import com.yn.printer.service.modules.meta.enums.PrintColor;
import com.yn.printer.service.modules.meta.enums.PrintDirection;
import com.yn.printer.service.modules.meta.enums.PrintFaces;
import com.yn.printer.service.modules.meta.enums.PrintType;
import com.yn.printer.service.modules.operation.entity.DevicesList;
import com.yn.printer.service.modules.orders.entity.OrderManagement;
import com.yn.printer.service.modules.orders.enums.PrintTaskStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 打印记录
 *
 * @author : Jonas Chan
 * @since : 2024/1/4 15:22
 */
@Data
@ApiModel(value = "PrintTaskVo", description = "打印任务")
public class PrintTaskVo {

  @ApiModelProperty(value = "打印状态 TO_BE_PRINTED(\"待打印\"),\n" +
          "  TO_BE_PUSH(\"待推送\"),\n" +
          "  PUSHED(\"已推送\"),\n" +
          "  PRINTING(\"打印中\"),\n" +
          "  COMPLETE(\"已完成\"),\n" +
          "  CANCELED(\"已取消\"),\n" +
          "  INTERRUPT(\"已中断\")")
  private PrintTaskStatus printTaskStatus;

  @ApiModelProperty(value = "打印类型 DocumentPrinting(\"文档打印\"),\n" +
          "  PhotoPrinting(\"照片打印\"),\n" +
          "  IdPrinting(\"证件照打印\")")
  private PrintType printType;

  @ApiModelProperty(value = "打印颜色 BW(\"黑白\"),\n" +
          "  COLOR(\"彩色\")")
  private PrintColor printingColor;

  @ApiModelProperty(value = "打印面数 SINGLE(\"单面\"),\n" +
          "  DOUBLE(\"双面\")")
  private PrintFaces printingFaces;

  @ApiModelProperty(value = "打印方向 VERTICAL(\"垂直\"),\n" +
          "  LEVEL(\"水平\")")
  private PrintDirection printDirection;

  @ApiModelProperty(value = "打印份数")
  private Integer copies = 0;

  @ApiModelProperty(value = "文件预览地址")
  private String filePreviewUrl;

  @ApiModelProperty(value = "文件名")
  private String fileName;

}
