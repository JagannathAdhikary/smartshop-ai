package com.smartshop.user.service;

import com.smartshop.user.repository.UserRepository;
import lombok.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  private final UserRepository userRepository;

  UserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  @NonNull
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
    return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email is not present"));
  }
}
