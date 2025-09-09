package org.sds.elevateconnect.utils;

import org.sds.elevateconnect.model.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result ex(Exception e){
        e.printStackTrace();
        return Result.error(e.getMessage());
    }
}
