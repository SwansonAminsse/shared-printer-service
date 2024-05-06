package com.yn.printer.service.advice;


import com.yn.printer.service.common.vo.ResponseVO;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


/**
 * Spring MVC 统一响应对象处理
 *
 * @author huabiao
 * @create 2023/12/08  15:42
 */
@ControllerAdvice(basePackages = "com.yn.printer.service.modules")
public class MyResponseBodyAdvice implements ResponseBodyAdvice<Object> {


    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object object, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (object == null) {
            object = ResponseVO.newInstance(null);
        }
        if (!(object instanceof ResponseVO)) {
            object = ResponseVO.newInstance(object);
        }
        return object;
    }
}
