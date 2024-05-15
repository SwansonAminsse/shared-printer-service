package com.yn.printer.service.modules.operation.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Data
@ApiModel(value = "PreprintTask", description = "预打印任务")
public class PrintTaskListDTO {
    @ApiModelProperty(value = "设备id")
    Long deviceId;
    @ApiModelProperty(value = "预打印任务")
    List<PreprintTask> preprintTaskList;


}