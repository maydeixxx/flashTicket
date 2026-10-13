package com.FlashTicket.UserService.api;

import com.FlashTicket.UserService.models.dtos.ErrorResponseDTO;
import com.FlashTicket.UserService.models.exceptions.AuthenticationErrorException;
import com.FlashTicket.UserService.models.exceptions.UserExistsException;
import com.FlashTicket.UserService.models.exceptions.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationErrorException.class)
    public ResponseEntity<ErrorResponseDTO> handleAuthenticationErrorException(AuthenticationErrorException ex, WebRequest request) {
        ErrorResponseDTO errorResponse = buildErrorResponse("Authentication error", ex.getMessage(), 400, request);
        return ResponseEntity.status(errorResponse.code()).body(errorResponse);
    }

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserExistsException(UserExistsException ex, WebRequest request) {
        ErrorResponseDTO errorResponse = buildErrorResponse("User exists", ex.getMessage(), 409, request);
        return ResponseEntity.status(errorResponse.code()).body(errorResponse);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        ErrorResponseDTO errorResponse = buildErrorResponse("User not found", ex.getMessage(), 404, request);
        return ResponseEntity.status(errorResponse.code()).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        String errorMessage = ex.getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));
        ErrorResponseDTO errorResponse = buildErrorResponse("Validation error", errorMessage, 400, request);

        return ResponseEntity.status(errorResponse.code()).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleException(Exception ex, WebRequest request) {
        ErrorResponseDTO errorResponse = buildErrorResponse("Server error", ex.getMessage(), 500, request);
        return ResponseEntity.status(errorResponse.code()).body(errorResponse);
    }

    private ErrorResponseDTO buildErrorResponse(String error, String errorMessage, int code, WebRequest request) {
        try {
            return new ErrorResponseDTO(error, errorMessage, code, request.getDescription(false).replace("uri=", ""), LocalDateTime.now());
        } catch (Exception e) {
            log.error("Error building error response: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
