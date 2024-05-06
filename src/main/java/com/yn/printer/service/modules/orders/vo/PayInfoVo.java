package com.yn.printer.service.modules.orders.vo;

import com.yn.printer.service.modules.common.api.wx.dto.JsPayInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 支付信息
 *
 * @author : Jonas Chan
 * @since : 2023/12/26 16:44
 */
@Data
@ApiModel(value = "PayInfoVo", description = "支付信息")
public class PayInfoVo {

    @ApiModelProperty(value = "支付码地址")
    String qrCode;

    @ApiModelProperty(value = "小程序支付信息")
    JsPayInfo jsPayInfo;

}
