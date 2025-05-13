package org.example.stage5.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Standard response wrapper for all API responses.
 * Provides consistent structure for all API responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StandardResponse {
    /**
     * Status of the response (success, error, warning, etc.)
     */
    private String status;
    
    /**
     * Payload data of the response
     */
    private Object data;
    
    /**
     * Error object in case of errors
     */
    private Object error;
    
    /**
     * Timestamp of the response
     */
    private LocalDateTime timestamp;
    
    public StandardResponse(String status, Object data, Object error) {
        this.status = status;
        this.data = data;
        this.error = error;
        this.timestamp = LocalDateTime.now();
    }
}