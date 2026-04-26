package com.hotel.paraiso.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * Manejador global de excepciones.
 * Intercepta todas las excepciones y retorna respuestas JSON estandarizadas.
 */
@RestControllerAdvice
@Slf4j  
public class GlobalExceptionHandler {

    // ─── 404 - Recurso no encontrado ─────────────────
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex, WebRequest request) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "NOT_FOUND",
                ex.getMessage(),
                request.getDescription(false)
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // ─── 400 - Solicitud inválida ─────────────────────
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(
            BadRequestException ex, WebRequest request) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "BAD_REQUEST",
                ex.getMessage(),
                request.getDescription(false)
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // ─── 422 - Regla de negocio violada ──────────────
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex, WebRequest request) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "BUSINESS_RULE_VIOLATION",
                ex.getMessage(),
                request.getDescription(false)
        );
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }

    // ─── 400 - Validaciones con @Valid ────────────────
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex, WebRequest request) {

        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String campo = ((FieldError) error).getField();
            String mensaje = error.getDefaultMessage();
            errores.put(campo, mensaje);
        });

        ValidationErrorResponse response = new ValidationErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_ERROR",
                "Error de validación en los campos",
                request.getDescription(false),
                errores
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // ─── 500 - Error interno del servidor ────────────
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, WebRequest request) {

                log.error("ERROR INTERNO: ", ex);

        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_SERVER_ERROR",
                "Ocurrió un error inesperado en el servidor",
                request.getDescription(false)
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    // ─── Clases de respuesta de error ────────────────

    public record ErrorResponse(
            int status,
            String codigo,
            String mensaje,
            String ruta,
            LocalDateTime timestamp
    ) {
        public ErrorResponse(int status, String codigo, String mensaje, String ruta) {
            this(status, codigo, mensaje, ruta, LocalDateTime.now());
        }
    }

    public record ValidationErrorResponse(
            int status,
            String codigo,
            String mensaje,
            String ruta,
            Map<String, String> errores,
            LocalDateTime timestamp
    ) {
        public ValidationErrorResponse(int status, String codigo, String mensaje,
                                        String ruta, Map<String, String> errores) {
            this(status, codigo, mensaje, ruta, errores, LocalDateTime.now());
        }
    }
}
