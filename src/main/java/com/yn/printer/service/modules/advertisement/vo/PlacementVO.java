package com.yn.printer.service.modules.advertisement.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "PlacementVO", description = "广告vo")
public class PlacementVO {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "预览地址")
    private String previewPath;

    @ApiModelProperty(value = "文件类型")
    private String fileType;

    @ApiModelProperty(value = "轮播时间")
    private Integer intervalTime;
}