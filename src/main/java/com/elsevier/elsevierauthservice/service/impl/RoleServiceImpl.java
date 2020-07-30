package com.elsevier.elsevierauthservice.service.impl;

import com.elsevier.elsevierauthservice.domain.Role;
import com.elsevier.elsevierauthservice.domain.RoleName;
import com.elsevier.elsevierauthservice.repository.RoleRepository;
import com.elsevier.elsevierauthservice.service.RoleService;
import com.elsevier.elsevierauthservice.shared.dto.RoleDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

  private final RoleRepository roleRepository;

  private final ModelMapper modelMapper;

  public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper) {
    this.roleRepository = roleRepository;
    this.modelMapper = modelMapper;
  }

  @Override
  public RoleDto getByRoleName(RoleName roleName) {
    Role role =
        roleRepository
            .findByName(roleName)
            .orElseThrow(() -> new RuntimeException("Role with name " + roleName + " not found"));

    return modelMapper.map(role, RoleDto.class);
  }

  @Override
  public RoleDto createRole(RoleDto roleDto) {
    Role role = modelMapper.map(roleDto, Role.class);
    Role savedRole = roleRepository.save(role);
    return modelMapper.map(savedRole, RoleDto.class);
  }
}
