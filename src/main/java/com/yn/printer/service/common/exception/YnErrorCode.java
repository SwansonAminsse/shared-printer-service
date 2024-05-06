package com.yn.printer.service.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 异常编码实体
 *
 * @author huabiao
 * @create 2023/12/08  15:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class YnErrorCode {

    private static final long SERIAL_VERSION_UID = 1L;

    private String status;
    private String msg;

    public static long getSerialVersionUid() {
        return SERIAL_VERSION_UID;
    }

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }
}
