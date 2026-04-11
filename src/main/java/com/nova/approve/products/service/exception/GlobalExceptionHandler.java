package com.nova.approve.products.service.exception;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // =========================
    // QUERY PARAMS (GET)
    // =========================
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {

        String field = ex.getName();
        String value = ex.getValue() != null ? ex.getValue().toString() : "null";

        log.warn("Invalid parameter format field={} value={}", field, value);

        Map<String, Object> response = Map.of(
                "error", "Invalid parameter format",
                "message", "Parameter '" + field + "' has invalid value '" + value + "'"
        );

        return ResponseEntity.badRequest().body(response);
    }

    // =========================
    // REQUEST BODY (PATCH / POST)
    // =========================
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidJson(
            HttpMessageNotReadableException ex) {

        log.warn("Invalid request body format: {}", ex.getMessage());

        Map<String, Object> response = Map.of(
                "error", "Invalid request body",
                "message", "Malformed JSON or incorrect data types"
        );

        return ResponseEntity.badRequest().body(response);
    }

    // =========================
    // GENERIC (fallback)
    // =========================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {

        log.error("Unexpected error: {}", ex.getMessage());

        Map<String, Object> response = Map.of(
                "error", "Internal server error",
                "message", "Something went wrong"
        );
        return ResponseEntity.internalServerError().body(response);
    }
}