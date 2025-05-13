package org.example.stage5.exception;

/**
 * Exception for invalid request data
 */
public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }
}