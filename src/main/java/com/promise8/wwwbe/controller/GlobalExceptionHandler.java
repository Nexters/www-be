package com.promise8.wwwbe.controller;

import com.promise8.wwwbe.model.exception.BizException;
import com.promise8.wwwbe.model.http.BaseErrorCode;
import com.promise8.wwwbe.model.http.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {BizException.class})
    public BaseResponse<String> handleBizException(BizException e) {
        log.error("biz exception", e);
        return BaseResponse.error(e.getCode());
    }

    @ExceptionHandler(value = {Exception.class})
    public BaseResponse<String> handleException(Exception e) {
        log.error("handle exception", e);
        return BaseResponse.error(BaseErrorCode.SERVER_ERROR);
    }
}
