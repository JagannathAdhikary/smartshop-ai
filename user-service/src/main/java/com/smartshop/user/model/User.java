package com.smartshop.user.model;

import com.smartshop.user.contant.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@Entity
@Table(name = "user_profile")
public class User implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID uuid;

  @NotBlank
  private String name;

  @Column(unique = true)
  @NotBlank
  private String email;

  @Column(unique = true)
  private String mobile;

  private String password;

  @Enumerated(EnumType.STRING)
  private UserRole role;

  private Instant createdAt;

  @Override
  @NonNull
  public String getUsername() {
    return this.email;
  }

  @Override
  @NonNull
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_"+this.role.name()));
  }
}
