package com.yn.printer.service.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 异常DTO
 *
 * @author huabiao
 * @create 2023/12/08  15:42
 */
@Data
public class ExceptionDTO implements Serializable {

    @ApiModelProperty(value = "错误信息", required = true)
    String error;

    @ApiModelProperty(value = "消息", required = true)
    String message;

    @ApiModelProperty(value = "请求地址", required = true)
    String path;

    @ApiModelProperty(value = "状态码", required = true)
    Integer status;

    @ApiModelProperty(value = "编码", required = true)
    String code;
}
