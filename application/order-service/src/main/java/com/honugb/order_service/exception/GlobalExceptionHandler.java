package com.honugb.order_service.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(
            OrderNotFoundException exception) {

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException exception) {

        Map<String, String> errors = new LinkedHashMap<>();

        exception.getBindingResult()
            .getFieldErrors()
            .forEach(error -> errors.put(
                error.getField(),
                error.getDefaultMessage()
            ));

        Map<String, Object> response = Map.of(
            "error", "Validation failed",
            "details", errors
        );

        return ResponseEntity
            .badRequest()
            .body(response);
    }
}