package com.elsevier.elsevierauthservice.service.impl;

import com.elsevier.elsevierauthservice.security.UserPrincipal;
import com.elsevier.elsevierauthservice.domain.User;
import com.elsevier.elsevierauthservice.repository.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Primary
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  public UserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String usernameOrEmail) {
    User user =
        userRepository
            .findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
            .orElseThrow(
                () ->
                    new UsernameNotFoundException(
                        "User not found with username or email : " + usernameOrEmail));

    return UserPrincipal.create(user);
  }

  @Transactional
  public UserDetails loadUserById(UUID id) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + id));

    return UserPrincipal.create(user);
  }
}
