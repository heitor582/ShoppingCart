package com.study.cart.api;

import com.study.cart.exception.NoStackTraceException;
import com.study.cart.exception.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<Object> handleNotFound(final NoStackTraceException e) {
        return ResponseEntity.badRequest().body(ApiError.from(e));
    }

    record ApiError(
            String message
    ) {
        static ApiError from(final NoStackTraceException exception) {
            return new ApiError(exception.getMessage());
        }
    }
}
