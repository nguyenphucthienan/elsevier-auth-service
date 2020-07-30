package com.elsevier.elsevierauthservice.service;

import com.elsevier.elsevierauthservice.shared.dto.UserDto;

import java.util.UUID;

public interface UserService {

  String authenticateUser(String usernameOrEmail, String password);

  UserDto getUserById(UUID id);

  UserDto createUser(UserDto userDto);

  UserDto updateUser(UUID id, UserDto userDto);

  Boolean checkUsernameAvailability(String username);

  Boolean checkEmailAvailability(String email);
}
