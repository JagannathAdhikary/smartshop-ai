package com.smartshop.user.controller;

import com.smartshop.user.dto.RegisterRequest;
import com.smartshop.user.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final UserService userService;

  @PostMapping("/register")
  public ResponseEntity register(@Valid @RequestBody RegisterRequest registerRequest) {
    if(userService.fetchUser(registerRequest.getEmail())!=null){
      return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
    }
    try {
      userService.save(registerRequest);
    } catch(DataIntegrityViolationException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Email uniqueness constaint violated");
    } catch(Exception e) {
      return ResponseEntity.status(500).body("Server Failure");
    }
    return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
  }

}
