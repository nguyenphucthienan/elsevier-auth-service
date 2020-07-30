package com.elsevier.elsevierauthservice.shared.dto;

import com.elsevier.elsevierauthservice.domain.RoleName;

import java.util.UUID;

public class RoleDto {

  private UUID id;
  private RoleName name;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public RoleName getName() {
    return name;
  }

  public void setName(RoleName name) {
    this.name = name;
  }
}
