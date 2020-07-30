package com.elsevier.elsevierauthservice.service;

import com.elsevier.elsevierauthservice.domain.RoleName;
import com.elsevier.elsevierauthservice.shared.dto.RoleDto;

public interface RoleService {

  RoleDto getByRoleName(RoleName roleName);

  RoleDto createRole(RoleDto roleDto);
}
