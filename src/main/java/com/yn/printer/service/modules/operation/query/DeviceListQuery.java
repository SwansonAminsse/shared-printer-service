package com.yn.printer.service.modules.operation.query;

import com.yn.printer.service.modules.common.query.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author : Jonas Chan
 * @since : 2023/12/18 17:51
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "DeviceListQuery", description = "设备列表查询条件")
public class DeviceListQuery extends BaseQuery {

    @ApiModelProperty(value = "经度")
    BigDecimal jd;

    @ApiModelProperty(value = "维度")
    BigDecimal wd;

}
