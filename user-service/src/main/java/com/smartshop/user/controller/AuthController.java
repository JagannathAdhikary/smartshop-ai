package com.smartshop.user.controller;

import com.smartshop.user.dto.RegisterRequest;
import com.smartshop.user.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
    userService.register(registerRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
  }

}
