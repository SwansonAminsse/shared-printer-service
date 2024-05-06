package com.yn.printer.service.modules.operation.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ConsumablesValueVo", description = "剩余耗材")
public class ConsumablesValueVo {

    @ApiModelProperty(value = "耗材类型")
    private String name;

    @ApiModelProperty(value = "剩余耗材")
    private Integer consumablesValue = 0;

}
