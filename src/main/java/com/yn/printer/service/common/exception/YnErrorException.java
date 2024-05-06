package com.yn.printer.service.common.exception;


import java.io.Serializable;

/**
 * 异常处理
 *
 * @author huabiao
 * @create 2023/12/08  15:42
 */
public class YnErrorException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;

    public YnErrorException(YnErrorCode ynErrorCode) {
        super(ynErrorCode.getStatus() + "#" + ynErrorCode.getMsg());
    }

    public YnErrorException(YnErrorCode ynErrorCode, String additionalString) {
        super(ynErrorCode.getStatus() + "#" + ynErrorCode.getMsg() + ":" + additionalString);
    }

    public YnErrorException(String code, String message) {
        super(code + "#" + message);
    }

    public YnErrorException(String message) {
        super(500 + "#" + message);
    }

    /**
     * 重写此方法返回this,业务异常不打印堆栈信息提高速度
     */
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}