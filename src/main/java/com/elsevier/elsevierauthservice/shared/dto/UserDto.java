package com.elsevier.elsevierauthservice.shared.dto;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class UserDto {

  private UUID id;
  private String username;
  private String email;
  private String firstName;
  private String lastName;
  private String password;
  private Set<RoleDto> roles = new HashSet<>();

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Set<RoleDto> getRoles() {
    return roles;
  }

  public void setRoles(Set<RoleDto> roles) {
    this.roles = roles;
  }
}
