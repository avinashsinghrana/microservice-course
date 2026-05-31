package com.asr.hotel.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class ApiResponse {
    private String message;
    private boolean success;
}
