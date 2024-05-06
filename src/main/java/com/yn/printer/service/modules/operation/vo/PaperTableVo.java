package com.yn.printer.service.modules.operation.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "PaperTableVo", description = "剩余纸张")
public class PaperTableVo {

    @ApiModelProperty(value = "纸张类型")
    private String name;

    @ApiModelProperty(value = "剩余纸张")
    private Integer residue = 0;

}
