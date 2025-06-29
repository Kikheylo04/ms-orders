package com.example.ms_orders.exception;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ErrorDetails {
     private LocalDateTime timestamp;
    private String message;
    private String details;
    private String errorCode;

    public ErrorDetails(LocalDateTime timestamp, String message, String details, String errorCode) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
        this.errorCode = errorCode;
    }
    
    public ErrorDetails(LocalDateTime timestamp, String message, String details) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }
}
