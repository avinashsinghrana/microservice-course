package com.asr.hotel.exception;

import com.asr.hotel.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HotelServiceException.class)
    public ResponseEntity<ApiResponse> handleHotelServiceException(HotelServiceException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.builder()
                .message(ex.getMessage())
                .success(false)
                .build());
    }
}
