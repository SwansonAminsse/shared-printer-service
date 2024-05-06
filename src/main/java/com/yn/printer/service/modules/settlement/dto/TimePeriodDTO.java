package com.yn.printer.service.modules.settlement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@ApiModel(value = "TimePeriodDTO", description = "时间段")
@JsonFormat(pattern = "yyyy-MM-dd")
public class TimePeriodDTO {
    @ApiModelProperty(value = "开始时间")
    private LocalDate start;

    @ApiModelProperty(value = "结束时间")
    private LocalDate end;


}