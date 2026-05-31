package com.asr.user.service.exception;

import com.asr.user.service.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserServiceException.class)
    public ResponseEntity<ApiResponse> handleUserServiceException(UserServiceException ex) {
        return new ResponseEntity<>(ApiResponse.builder().message(ex.getMessage()).success(false).build(), HttpStatus.NOT_FOUND);
    }

}
