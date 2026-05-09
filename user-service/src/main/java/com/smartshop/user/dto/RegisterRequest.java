package com.smartshop.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
  @NotBlank
  private String name;
  @NotBlank
  private String email;
  @NotBlank
  private String password;

  private String mobile;

  private String role;
}
