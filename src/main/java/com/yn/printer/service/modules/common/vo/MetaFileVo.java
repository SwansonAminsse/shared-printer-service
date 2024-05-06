package com.yn.printer.service.modules.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author : Jonas Chan
 * @since : 2023/12/18 10:23
 */
@Data
@ApiModel(value = "FileVo", description = "文件vo")
public class MetaFileVo {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "文件地址")
    private String filePath;

    @ApiModelProperty(value = "预览地址")
    private String previewPath;

    @ApiModelProperty(value = "下载地址")
    private String downloadPath;

    @ApiModelProperty(value = "外链地址")
    private String outerChainPath;

    @ApiModelProperty(value = "文件大小")
    private Long fileSize = 0L;

    @ApiModelProperty(value = "文件类型")
    private String fileType;

    @ApiModelProperty(value = "文件描述")
    private String description;

    @ApiModelProperty(value = "PDF页数")
    private Integer pdfPageSize;
}
