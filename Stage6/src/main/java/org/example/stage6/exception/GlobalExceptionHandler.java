package org.example.stage6.exception;

import org.example.stage6.response.StandardResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

/** 
 * GlobalExceptionHandler is responsible for handling exceptions across the entire application.
 * This class has a lower priority than AdminExceptionHandler and will only handle exceptions
 * that are not specifically handled by more specialized exception handlers.
 * 
 * It provides standardized error responses for REST API calls, ensuring consistent
 * error formatting across the application.
 */
@ControllerAdvice
@Order // Default order - lower priority than HIGHEST_PRECEDENCE
public class GlobalExceptionHandler {

    // משאיר את כל ה-handlers הקיימים
    @ExceptionHandler(NotExists.class)
    public ResponseEntity<StandardResponse> handleNotExists(NotExists ex, WebRequest request) {
        Map<String, String> details = new HashMap<>();
        details.put("type", "Resource Not Found");
        details.put("message", ex.getMessage());
        
        StandardResponse response = new StandardResponse("error", null, details);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyExists.class)
    public ResponseEntity<StandardResponse> handleAlreadyExists(AlreadyExists ex, WebRequest request) {
        Map<String, String> details = new HashMap<>();
        details.put("type", "Resource Conflict");
        details.put("message", ex.getMessage());
        
        StandardResponse response = new StandardResponse("error", null, details);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(StudentIdAndIdMismatch.class)
    public ResponseEntity<StandardResponse> handleIdMismatch(StudentIdAndIdMismatch ex, WebRequest request) {
        Map<String, String> details = new HashMap<>();
        details.put("type", "ID Mismatch");
        details.put("message", ex.getMessage());
        
        StandardResponse response = new StandardResponse("error", null, details);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<StandardResponse> handleInvalidRequest(InvalidRequestException ex, WebRequest request) {
        Map<String, String> details = new HashMap<>();
        details.put("type", "Invalid Request");
        details.put("message", ex.getMessage());
        
        StandardResponse response = new StandardResponse("error", null, details);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        Map<String, Object> details = new HashMap<>();
        details.put("type", "Validation Failed");
        details.put("fields", errors);
        
        StandardResponse response = new StandardResponse("error", null, details);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /*
    Security-related exceptions are handled here to provide a consistent response format
     */

    /**
     * Handles security-related authentication exceptions (401 Unauthorized)
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<StandardResponse> handleAuthException(
            AuthenticationException ex, WebRequest request) {
        Map<String, String> details = new HashMap<>();
        details.put("type", "Authentication Error");
        details.put("message", ex.getMessage());
        
        StandardResponse response = new StandardResponse("error", null, details);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
    
    /**
     * Handles access denied exceptions (403 Forbidden)
     * Note: This will only handle access denied for REST API calls.
     * The AdminExceptionHandler will handle access denied for web views.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<StandardResponse> handleAccessDeniedException(
            AccessDeniedException ex, WebRequest request) {
        Map<String, String> details = new HashMap<>();
        details.put("type", "Access Denied");
        details.put("message", "You don't have permission to access this resource");
        
        StandardResponse response = new StandardResponse("error", null, details);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
    
    /**
     * Handles bad credentials exceptions with more specific message
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<StandardResponse> handleBadCredentialsException(
            BadCredentialsException ex, WebRequest request) {
        Map<String, String> details = new HashMap<>();
        details.put("type", "Authentication Error");
        details.put("message", "Invalid username or password");
        
        StandardResponse response = new StandardResponse("error", null, details);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
    
    /**
     * Handles account disabled exceptions
     */
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<StandardResponse> handleDisabledException(
            DisabledException ex, WebRequest request) {
        Map<String, String> details = new HashMap<>();
        details.put("type", "Account Error");
        details.put("message", "Account is disabled");
        
        StandardResponse response = new StandardResponse("error", null, details);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
    
    /**
     * Handles account locked exceptions
     */
    @ExceptionHandler(LockedException.class)
    public ResponseEntity<StandardResponse> handleLockedException(
            LockedException ex, WebRequest request) {
        Map<String, String> details = new HashMap<>();
        details.put("type", "Account Error");
        details.put("message", "Account is locked");
        
        StandardResponse response = new StandardResponse("error", null, details);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardResponse> handleGenericException(Exception ex, WebRequest request) {
        Map<String, String> details = new HashMap<>();
        details.put("type", "Internal Server Error");
        details.put("message", ex.getMessage());
        
        StandardResponse response = new StandardResponse("error", null, details);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}