package com.FlashTicket.InventoryService.api;

import com.FlashTicket.InventoryService.models.dtos.ErrorResponseDTO;
import com.FlashTicket.InventoryService.models.exceptions.EventNotFoundException;
import com.FlashTicket.InventoryService.models.exceptions.UpdateStrategyNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class InventoryExceptionHandler {
    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleEventNotFoundException(EventNotFoundException ex, WebRequest request) {
        log.error("Event not found: ", ex);
        ErrorResponseDTO errorResponse = buildErrorResponse("Event not found", ex.getMessage(), 404, request);
        return ResponseEntity.status(errorResponse.code()).body(errorResponse);
    }

    @ExceptionHandler(UpdateStrategyNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUpdateStrategyNotFoundException(UpdateStrategyNotFoundException ex, WebRequest request) {
        log.error("Update strategy not found: ", ex);
        ErrorResponseDTO errorResponse = buildErrorResponse("Update strategy not found", ex.getMessage(), 404, request);
        return ResponseEntity.status(errorResponse.code()).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleException(Exception ex, WebRequest request) {
        log.error("Server error: ", ex);
        ErrorResponseDTO errorResponse = buildErrorResponse("Server error", ex.getMessage(), 500, request);
        return ResponseEntity.status(errorResponse.code()).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        log.error("Illegal argument: ", ex);
        ErrorResponseDTO errorResponse = buildErrorResponse("Illegal argument", ex.getMessage(), 400, request);
        return ResponseEntity.status(errorResponse.code()).body(errorResponse);
    }

    private ErrorResponseDTO buildErrorResponse(String error, String message, int code, WebRequest request) {
        return new ErrorResponseDTO(
                error,
                message,
                code,
                request.getDescription(false).replace("uri=", ""),
                LocalDateTime.now()
        );
    }
}
