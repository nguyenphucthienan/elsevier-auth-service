package com.elsevier.elsevierauthservice.bootstrap;

import com.elsevier.elsevierauthservice.domain.RoleName;
import com.elsevier.elsevierauthservice.service.RoleService;
import com.elsevier.elsevierauthservice.shared.dto.RoleDto;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements CommandLineRunner {

  private final RoleService roleService;

  public Bootstrap(RoleService roleService) {
    this.roleService = roleService;
  }

  @Override
  public void run(String... args) throws Exception {
    initRole();
  }

  private void initRole() {
    try {
      RoleDto adminRoleDto = roleService.getByRoleName(RoleName.ROLE_ADMIN);
    } catch (Exception e) { // Admin role not found
      RoleDto adminRoleDto = new RoleDto();
      adminRoleDto.setName(RoleName.ROLE_ADMIN);

      RoleDto userRoleDto = new RoleDto();
      userRoleDto.setName(RoleName.ROLE_USER);

      roleService.createRole(adminRoleDto);
      roleService.createRole(userRoleDto);
    }
  }
}
