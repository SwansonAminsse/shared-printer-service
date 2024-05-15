package com.yn.printer.service.modules.orders.dto;

import com.yn.printer.service.modules.enums.PayMode;
import com.yn.printer.service.modules.operation.entity.TaskList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel(value = "SubmitRechargeTaskDto", description = "提交充值任务")
public class PrintInfoDTO {
    @ApiModelProperty(value = "设备id")
    Long deviceId;

    @ApiModelProperty(value = "打印任务")
    List<TaskList> taskLists;
}