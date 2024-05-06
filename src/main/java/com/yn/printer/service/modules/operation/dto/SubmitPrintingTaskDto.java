package com.yn.printer.service.modules.operation.dto;

import com.yn.printer.service.modules.enums.PayMode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "SubmitPrintingTaskDto", description = "提交打印任务")
public class SubmitPrintingTaskDto {

    @ApiModelProperty(value = "支付方式 " +
            "  TZ_PAY(\"TZ_PAY\", \"天章支付\"),\n" +
            "  WX_NATIVE_PAY(\"WX_PAY\", \"微信本地支付\"),\n" +
            "  WX_JS_PAY(\"WX_JS_PAY\", \"微信小程序支付\"),\n"+
            "  YU_E_PAY(\"YU_E_PAY\", \"余额支付\"),\n"
    )
    PayMode payMode;

    @ApiModelProperty(value = "设备id")
    Long deviceId;

    @ApiModelProperty(value = "任务列表")
    List<PreprintTask> taskList;

}
