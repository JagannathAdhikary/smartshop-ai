package com.smartshop.user.controller;

import com.smartshop.user.model.User;
import com.smartshop.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity fetchMe(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.fetchUserProfile(user.getUsername()));
    }
}
