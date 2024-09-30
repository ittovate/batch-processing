package com.ittovative.batchprocessing.exception;

import com.ittovative.batchprocessing.util.APIResponse;
import com.ittovative.batchprocessing.util.ResponseUtil;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * The type Global exception handler.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle response entity.
     *
     * @param exception the exception
     * @return the response entity
     */
    @ExceptionHandler(Exception.class)
    public APIResponse<String> handle(Exception exception) {
        exception.fillInStackTrace();
        return ResponseUtil.createUnifiedResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),null);
    }

}
