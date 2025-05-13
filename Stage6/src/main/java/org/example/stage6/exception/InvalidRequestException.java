package org.example.stage6.exception;

/**
 * Exception for invalid request data
 */
public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }
}