package com.smartshop.user.service;

import com.smartshop.user.constant.UserRole;
import com.smartshop.user.dto.RegisterRequest;
import com.smartshop.user.model.User;
import com.smartshop.user.repository.UserRepository;
import java.time.Instant;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public void save(RegisterRequest registerRequest) {
    User user = convertToUser(registerRequest);
    userRepository.save(user);
  }

  public User fetchUser(String email) {
    return userRepository.findByEmail(email).orElse(null);
  }

  private User convertToUser(RegisterRequest request) {
    User user = new User();
    user.setName(request.getName());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setMobile(request.getMobile());
    user.setRole(UserRole.valueOf(request.getRole()));
    user.setCreatedAt(Instant.now());
    return user;
  }
}
