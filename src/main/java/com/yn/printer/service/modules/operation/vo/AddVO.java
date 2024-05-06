package com.yn.printer.service.modules.operation.vo;

import com.yn.printer.service.modules.operation.enums.ProcessingStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class AddVO {
    @ApiModelProperty(value = "完成时间")
    private LocalDateTime completionTime;
    @ApiModelProperty(value = "已处理未处理")
    private ProcessingStatus taskStatus;

    public AddVO(LocalDateTime completionTime, ProcessingStatus taskStatus) {
        this.completionTime = completionTime;
        this.taskStatus = taskStatus;
    }
}
