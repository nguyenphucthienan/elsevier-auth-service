package com.elsevier.elsevierauthservice.service.impl;

import com.elsevier.elsevierauthservice.domain.Role;
import com.elsevier.elsevierauthservice.domain.RoleName;
import com.elsevier.elsevierauthservice.domain.User;
import com.elsevier.elsevierauthservice.repository.RoleRepository;
import com.elsevier.elsevierauthservice.repository.UserRepository;
import com.elsevier.elsevierauthservice.security.JwtTokenProvider;
import com.elsevier.elsevierauthservice.service.UserService;
import com.elsevier.elsevierauthservice.shared.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  private final RoleRepository roleRepository;

  private final AuthenticationManager authenticationManager;

  private final JwtTokenProvider jwtTokenProvider;

  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  private final ModelMapper modelMapper;

  public UserServiceImpl(
      UserRepository userRepository,
      RoleRepository roleRepository,
      AuthenticationManager authenticationManager,
      JwtTokenProvider jwtTokenProvider,
      BCryptPasswordEncoder bCryptPasswordEncoder,
      ModelMapper modelMapper) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.authenticationManager = authenticationManager;
    this.jwtTokenProvider = jwtTokenProvider;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.modelMapper = modelMapper;
  }

  @Override
  public String authenticateUser(String usernameOrEmail, String password) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(usernameOrEmail, password));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    return jwtTokenProvider.generateToken(authentication);
  }

  @Override
  public UserDto getUserById(UUID id) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("User with ID " + id + " not found"));

    return modelMapper.map(user, UserDto.class);
  }

  @Override
  public UserDto createUser(UserDto userDto) {
    if (userRepository
        .findByUsernameOrEmail(userDto.getUsername(), userDto.getEmail())
        .isPresent()) {
      throw new RuntimeException("Email already exists");
    }

    User user = modelMapper.map(userDto, User.class);
    user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));

    Role userRole =
        roleRepository
            .findByName(RoleName.ROLE_USER)
            .orElseThrow(() -> new RuntimeException("Role not found"));

    user.setRoles(Collections.singleton(userRole));
    User savedUser = userRepository.save(user);
    return modelMapper.map(savedUser, UserDto.class);
  }

  @Override
  public UserDto updateUser(UUID id, UserDto userDto) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("User with ID " + id + " not found"));

    user.setEmail(userDto.getEmail());
    user.setFirstName(userDto.getFirstName());
    user.setLastName(userDto.getLastName());

    User savedUser = userRepository.save(user);
    return modelMapper.map(savedUser, UserDto.class);
  }

  @Override
  public Boolean checkUsernameAvailability(String username) {
    return !userRepository.existsByUsername(username);
  }

  @Override
  public Boolean checkEmailAvailability(String email) {
    return !userRepository.existsByEmail(email);
  }
}
