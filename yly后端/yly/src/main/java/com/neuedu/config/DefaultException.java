package com.neuedu.config;

import com.neuedu.vo.ResultJson;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DefaultException {
    @ExceptionHandler(value = NeueduException.class)
    public ResultJson neueduExceptionHandler(NeueduException ex) {
        ex.printStackTrace();
        return ResultJson.failed(ex.getMessage());
    }
    @ExceptionHandler
    public ResultJson defaultExceptionHandler(Exception ex) {
        ex.printStackTrace();
        return ResultJson.failed("服务器异常");
    }
}
