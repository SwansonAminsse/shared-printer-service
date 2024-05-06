package com.yn.printer.service.modules.operation.vo;

import com.yn.printer.service.modules.meta.enums.PaperType;
import com.yn.printer.service.modules.operation.enums.ConsumableType;
import com.yn.printer.service.modules.operation.enums.OperationsType;
import com.yn.printer.service.modules.operation.enums.ProcessingStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class ReadVo {

    @ApiModelProperty(value = "运维类型")
    private OperationsType taskType;
    @ApiModelProperty(value = "设备编号")
    private String code;
    @ApiModelProperty(value = "任务id")
    private Long id;
    @ApiModelProperty(value = "已读未读")
    private Boolean readed;
    @ApiModelProperty(value = "已处理未处理")
    private ProcessingStatus taskStatus;

    @ApiModelProperty(value = "創建时间")
    private LocalDateTime createdOn;
    @ApiModelProperty(value = "完成时间")
    private LocalDateTime completionTime;
    @ApiModelProperty(value = "纸张类型")
    private PaperType paperType;
    @ApiModelProperty(value = "耗材类型")
    private ConsumableType consumable;
    public ReadVo(OperationsType taskType, Long id, String code, Boolean readed, ProcessingStatus taskStatus, LocalDateTime completionTime, LocalDateTime createdOn, PaperType paperType, ConsumableType consumable) {
        this.taskType = taskType;
        this.id = id;
        this.code = code;
        this.readed = readed;
        this.taskStatus = taskStatus;
        this.completionTime = completionTime;
        this.paperType = paperType;
        this.createdOn = createdOn;
        this.consumable = consumable;
    }
}
