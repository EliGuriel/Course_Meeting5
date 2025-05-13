package org.example.stage6.exception;

import org.example.stage6.response.StandardResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

/** TODO Stage6 AdminExceptionHandler
 * AdminExceptionHandler is a specialized exception handler for admin-related operations.
 * It has higher priority than the GlobalExceptionHandler to ensure admin-specific exceptions
 * are handled appropriately before falling back to the global handler.
 * 
 * This class handles exceptions thrown specifically in the admin controller package
 * and provides more specialized error handling including both web view responses
 * and API responses depending on the request type.
 */
@ControllerAdvice(basePackages = "org.example.stage6.controller")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AdminExceptionHandler {

    /**
     * Handles InvalidRequestException specifically for admin operations.
     * This method will return either:
     * - A view with error data for web requests
     * - A standardized JSON response for API requests
     * 
     * @param ex The InvalidRequestException that was thrown
     * @param request The web request context
     * @param redirectAttributes For adding flash attributes in case of redirect
     * @param model For adding attributes to the model in case of rendering a view
     * @return Either a view name or a ResponseEntity depending on the request type
     */
    @ExceptionHandler(InvalidRequestException.class)
    public Object handleAdminInvalidRequest(InvalidRequestException ex, 
                                           WebRequest request, 
                                           RedirectAttributes redirectAttributes,
                                           Model model) {
        
        // Check if it's an API request by examining the Accept header
        if (isApiRequest(request)) {
            // For API requests, return a JSON response with error details
            Map<String, String> details = new HashMap<>();
            details.put("type", "Admin Operation Error");
            details.put("message", ex.getMessage());
            
            StandardResponse response = new StandardResponse("error", null, details);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            // For web requests, prepare model attributes and return appropriate view
            if (request.getHeader("Referer") != null) {
                // If there's a referer header, redirect back with flash message
                redirectAttributes.addFlashAttribute("error", ex.getMessage());
                return "redirect:" + request.getHeader("Referer");
            } else {
                // Otherwise add to model and return error view
                model.addAttribute("error", ex.getMessage());
                return "error"; // Make sure this view template exists
            }
        }
    }
    
    /**
     * Helper method to determine if a request is an API request based on Accept header.
     * 
     * @param request The web request
     * @return True if the request expects JSON or XML response
     */
    private boolean isApiRequest(WebRequest request) {
        String acceptHeader = request.getHeader("Accept");
        return acceptHeader != null && 
               (acceptHeader.contains("application/json") || 
                acceptHeader.contains("application/xml"));
    }
    
    /**
     * Handles access denied exceptions for admin operations.
     * Returns a custom access-denied view with appropriate error message.
     * 
     * @param ex The AccessDeniedException that was thrown
     * @param model The model to add attributes to
     * @return The name of the access denied view
     */
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public String handleAccessDenied(Exception ex, Model model) {
        model.addAttribute("error", "אין לך הרשאות לגשת לדף זה");
        return "access-denied"; // Make sure this view template exists
    }
}