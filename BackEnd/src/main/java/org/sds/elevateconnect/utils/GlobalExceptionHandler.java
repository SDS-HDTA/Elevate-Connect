package org.sds.elevateconnect.utils;

import lombok.extern.slf4j.Slf4j;
import org.sds.elevateconnect.model.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result ex(Exception e){
        log.error(e.getMessage());
        return Result.error(e.getMessage());
    }
}
