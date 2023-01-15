package com.promise8.wwwbe.controller;

import com.promise8.wwwbe.model.exception.BizException;
import com.promise8.wwwbe.model.http.BaseResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {BizException.class})
    public BaseResponse<String> handleBizException() {
        
        // TODO: error 정의 필요
        return BaseResponse.ok("error", "error!!!");
    }
}
