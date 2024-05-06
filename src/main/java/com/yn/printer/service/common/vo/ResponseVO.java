package com.yn.printer.service.common.vo;

import com.yn.printer.service.common.dto.ExceptionDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;


/**
 * 统一的响应VO
 *
 * @author huabiao
 * @create 2023/12/08  15:42
 */
@Data
@ApiModel(value = "ResponseVO", description = "统一的响应VO")
public class ResponseVO<T> implements Serializable {

    /**
     * 编码
     */
    @ApiModelProperty(value = "编码")
    private String code;

    /**
     * 返回数据
     */
    @ApiModelProperty(value = "数据")
    private T data;

    /**
     * 消息
     */
    @ApiModelProperty(value = "消息")
    private String msg;

    public ResponseVO(T data) {
        this.code = String.valueOf(HttpStatus.OK.value());
        this.data = data;
    }

    public ResponseVO(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static <T> ResponseVO<T> newInstance(T data) {
        return new ResponseVO<T>(data);
    }

    public static <T> ResponseVO<T> newInstanceException(ExceptionDTO dto) {
        return new ResponseVO<T>(String.valueOf(dto.getCode()), dto.getMessage());
    }

    public static <T> ResponseVO<T> newInstance(String code, String msg) {
        return new ResponseVO<T>(code, msg);
    }
}
