package com.elsevier.elsevierauthservice.controller;

import com.elsevier.elsevierauthservice.exception.BadRequestException;
import com.elsevier.elsevierauthservice.payload.request.UserLoginRequest;
import com.elsevier.elsevierauthservice.payload.request.UserRegisterRequest;
import com.elsevier.elsevierauthservice.payload.request.UserUpdateRequest;
import com.elsevier.elsevierauthservice.payload.response.JwtAuthenticationResponse;
import com.elsevier.elsevierauthservice.payload.response.UserResponse;
import com.elsevier.elsevierauthservice.security.CurrentUser;
import com.elsevier.elsevierauthservice.security.UserPrincipal;
import com.elsevier.elsevierauthservice.service.UserService;
import com.elsevier.elsevierauthservice.shared.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final UserService userService;

  private final ModelMapper modelMapper;

  public AuthController(UserService userService, ModelMapper modelMapper) {
    this.userService = userService;
    this.modelMapper = modelMapper;
  }

  @PostMapping("/login")
  public JwtAuthenticationResponse login(@Valid @RequestBody UserLoginRequest userLoginRequest) {
    String jwt =
        userService.authenticateUser(
            userLoginRequest.getUsernameOrEmail(), userLoginRequest.getPassword());

    return new JwtAuthenticationResponse(jwt);
  }

  @PostMapping("/register")
  public UserResponse register(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
    if (!userService.checkUsernameAvailability(userRegisterRequest.getUsername())) {
      throw new BadRequestException("Username already taken");
    }

    if (!userService.checkEmailAvailability(userRegisterRequest.getEmail())) {
      throw new BadRequestException("Email address already in use");
    }

    UserDto userDto = modelMapper.map(userRegisterRequest, UserDto.class);
    UserDto createdUserDto = userService.createUser(userDto);
    return modelMapper.map(createdUserDto, UserResponse.class);
  }

  @GetMapping("/me")
  @PreAuthorize("hasRole('USER')")
  public UserResponse getCurrentUser(@CurrentUser UserPrincipal currentUser) {
    UserDto userDto = userService.getUserById(currentUser.getId());
    return modelMapper.map(userDto, UserResponse.class);
  }

  @PutMapping("/me")
  @PreAuthorize("hasRole('USER')")
  public UserResponse updateUserInfo(
      @CurrentUser UserPrincipal currentUser, @RequestBody UserUpdateRequest userUpdateRequest) {
    UserDto userDto = modelMapper.map(userUpdateRequest, UserDto.class);
    UserDto updatedUserDto = userService.updateUser(currentUser.getId(), userDto);
    return modelMapper.map(updatedUserDto, UserResponse.class);
  }
}
