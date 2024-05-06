package com.yn.printer.service.modules.operation.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "GoodsVo", description = "打印商品vo")
public class GoodsVo {

    @ApiModelProperty(value = "商品名称")
    String name;

    @ApiModelProperty(value = "商品价格")
    BigDecimal amount;



}
