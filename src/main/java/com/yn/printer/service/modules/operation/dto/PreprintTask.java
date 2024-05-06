package com.yn.printer.service.modules.operation.dto;

import com.yn.printer.service.modules.meta.enums.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author : Jonas Chan
 * @since : 2023/12/18 19:21
 */
@Data
@ApiModel(value = "PreprintTask", description = "预打印任务")
public class PreprintTask {

    @ApiModelProperty(value = "打印份数")
    Integer copies = 1;

    @NotNull
    @ApiModelProperty(value = "打印类型 DocumentPrinting(\"文档打印\"),\n" +
            "  PhotoPrinting(\"照片打印\"),\n" +
            "  IdPrinting(\"证件照打印\")")
    PrintType printType;

    @ApiModelProperty(value = "打印颜色 BW(\"黑白\"),\n" +
            "  COLOR(\"彩色\")")
    PrintColor printingColor;

    @ApiModelProperty(value = "打印面数 " +
            " SINGLE(\"单面\"),\n" +
            "  DOUBLE(\"双面\")")
    PrintFaces printingFaces;

    @ApiModelProperty(value = "打印方向 VERTICAL(\"垂直\"),\n" +
            "  LEVEL(\"水平\")")
    PrintDirection printDirection;

    @ApiModelProperty(value = "文件id")
    Long fileId;

    @ApiModelProperty(value = "源文件名")
    String sourceFileName;

}
