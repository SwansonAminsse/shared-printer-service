package com.yn.printer.service.common.annotation;

import java.lang.annotation.*;

/**
 * 将字段标记为使用指定的自定义序列
 *
 * @author huabiao
 * @create 2023/12/08  15:42
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Sequence {

    /**
     * 自定义序列名称
     */
    String value();
}
