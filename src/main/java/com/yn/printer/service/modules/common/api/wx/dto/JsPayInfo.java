package com.yn.printer.service.modules.common.api.wx.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 小程序支付信息
 *
 * @author : Jonas Chan
 * @since : 2023/12/26 16:44
 */
@Data
@ApiModel(value = "JsPayInfo", description = "小程序支付信息")
public class JsPayInfo {

    @ApiModelProperty(value = "时间戳")
    Long timeStamp;

    @ApiModelProperty(value = "随机字符串")
    String nonceStr;

    @ApiModelProperty(value = "订单详情扩展字符串")
    String packages;

    @ApiModelProperty(value = "签名方式")
    String signType;

    @ApiModelProperty(value = "签名")
    String paySign;

    public String toSignString(String appId) {

        // 每个参数后面都要加 \n !!!
        return appId + "\n" +
                this.timeStamp + "\n" +
                this.nonceStr + "\n" +
                this.packages + "\n";

    }

}
