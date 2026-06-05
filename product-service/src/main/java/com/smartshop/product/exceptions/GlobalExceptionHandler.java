package com.smartshop.product.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity productNotFoundException(ProductNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }
}
