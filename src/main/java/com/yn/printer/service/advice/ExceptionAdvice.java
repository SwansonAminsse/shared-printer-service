package com.yn.printer.service.advice;

import com.alibaba.fastjson.JSON;
import com.yn.printer.service.common.dto.ExceptionDTO;
import com.yn.printer.service.common.exception.YnError;
import com.yn.printer.service.common.exception.YnErrorException;
import com.yn.printer.service.common.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理Controller抛出的异常
 *
 * @author huabiao
 * @create 2023/12/08  15:42
 */
@Slf4j
@ControllerAdvice
public class ExceptionAdvice {

    /**
     * 全部位置异常-处理
     *
     * @param runtimeException 异常
     * @param request          请求
     * @param response         响应
     * @return ResponseVO
     * @author huabiao
     * @create 2023/12/8  16:25
     */
    @ExceptionHandler()
    @ResponseBody
    public ResponseVO handleRuntimeException(RuntimeException runtimeException, HttpServletRequest request, HttpServletResponse response) {
        ExceptionDTO exceptionDTO = new ExceptionDTO();
        exceptionDTO.setMessage("服务异常");
        exceptionDTO.setPath(request.getRequestURI());
        exceptionDTO.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        log.error(runtimeException.getMessage(), runtimeException);
        return ResponseVO.newInstanceException(exceptionDTO);
    }

    /**
     * 业务异常-处理
     *
     * @param ynErrorException 业务异常
     * @param request          请求
     * @param response         响应
     * @return ResponseVO
     * @author huabiao
     * @create 2023/12/8  16:27
     */
    @ExceptionHandler(YnErrorException.class)
    @ResponseBody
    public ResponseVO handleYnceErrorException(YnErrorException ynErrorException, HttpServletRequest request, HttpServletResponse response) {
        String[] str = ynErrorException.getMessage().split("#");
        ExceptionDTO exceptionDTO = new ExceptionDTO();
        exceptionDTO.setMessage(str[1]);
        exceptionDTO.setError(ynErrorException.toString());
        exceptionDTO.setPath(request.getRequestURI());
        exceptionDTO.setCode(str[0]);
        return ResponseVO.newInstanceException(exceptionDTO);
    }

    /**
     * 非法参数异常-处理
     *
     * @param methodArgumentNotValidException 非法参数异常
     * @param request                         请求
     * @param response                        响应
     * @return ResponseVO
     * @author huabiao
     * @create 2023/12/8  16:30
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseVO handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException, HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> errorMap = new HashMap<>(4);
        String fieldErrorMessage = null;
        String field = null;
        for (FieldError fieldError : methodArgumentNotValidException.getBindingResult().getFieldErrors()) {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            fieldErrorMessage = fieldError.getDefaultMessage();
            field = "(" + fieldError.getField() + ")";
        }
        ExceptionDTO dto = new ExceptionDTO();
        dto.setMessage(YnError.YN_100001.getMsg() + field + ":" + fieldErrorMessage);
        dto.setError(JSON.toJSONString(errorMap));
        dto.setPath(request.getRequestURI());
        dto.setCode(YnError.YN_100001.getStatus());
        return ResponseVO.newInstanceException(dto);
    }

    /**
     * 非法参数异常-处理
     *
     * @param constraintViolationException 非法参数异常
     * @param request                      请求
     * @param response                     响应
     * @return ResponseVO
     * @author huabiao
     * @create 2023/12/8  16:30
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseVO handleConstraintViolationException(ConstraintViolationException constraintViolationException, HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> errorMap = new HashMap<>(4);
        String fieldErrorMessage = null;
        String field = null;
        for (ConstraintViolation<?> constraintViolation : constraintViolationException.getConstraintViolations()) {
            errorMap.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
            fieldErrorMessage = constraintViolation.getMessage();
            field = "(" + constraintViolation.getPropertyPath().toString() + ")";
        }
        ExceptionDTO dto = new ExceptionDTO();
        dto.setMessage(YnError.YN_100001.getMsg() + field + ":" + fieldErrorMessage);
        dto.setError(JSON.toJSONString(errorMap));
        dto.setPath(request.getRequestURI());
        dto.setCode(YnError.YN_100001.getStatus());
        return ResponseVO.newInstanceException(dto);
    }

    /**
     * 序列化失败异常-处理
     *
     * @param httpMessageNotReadableException 序列化异常
     * @param request                         请求
     * @param response                        响应
     * @return ResponseVO
     * @description
     * @author huabiao
     * @create 2023/12/8  16:29
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseVO handleHttpMessageNotReadableException(HttpMessageNotReadableException httpMessageNotReadableException, HttpServletRequest request, HttpServletResponse response) {
        log.error("参数序列化失败：" + httpMessageNotReadableException.getMessage());
        ExceptionDTO exceptionDTO = new ExceptionDTO();
        exceptionDTO.setMessage(YnError.YN_100001.getMsg() + ":序列化失败");
        exceptionDTO.setPath(request.getRequestURI());
        exceptionDTO.setCode(YnError.YN_100001.getStatus());
        return ResponseVO.newInstanceException(exceptionDTO);
    }

    /**
     * 乐观锁异常-处理
     *
     * @param objectOptimisticLockingFailureException 乐观锁异常
     * @param request                                 请求
     * @param response                                响应
     * @return ResponseVO
     * @author huabiao
     * @create 2023/12/8  16:28
     */
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    @ResponseBody
    public ResponseVO handleStaleObjectStateException(ObjectOptimisticLockingFailureException objectOptimisticLockingFailureException, HttpServletRequest request, HttpServletResponse response) {
        log.error("乐观锁异常：" + objectOptimisticLockingFailureException.getMessage());
        ExceptionDTO exceptionDTO = new ExceptionDTO();
        exceptionDTO.setMessage(YnError.YN_000000.getMsg());
        exceptionDTO.setPath(request.getRequestURI());
        exceptionDTO.setCode(YnError.YN_000000.getStatus());
        return ResponseVO.newInstanceException(exceptionDTO);
    }
}
