package com.smartshop.user.exception;

import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity handleUserExist(UserAlreadyExistsException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity handleEmailUniquenessConflict(DataIntegrityViolationException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body("Email uniqueness constaint violated \n"+e.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity handleBadArgument(MethodArgumentNotValidException e) {
    String message = e.getBindingResult().getFieldErrors().stream()
        .map(err -> err.getRejectedValue()+" is not a valid "+err.getField()).collect(Collectors.joining("\n"));
    return ResponseEntity.badRequest().body(message);
  }
}
