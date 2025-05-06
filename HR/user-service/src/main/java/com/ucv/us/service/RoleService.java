package com.ucv.us.service;


import com.ucv.us.dto.RoleDTO;
import com.ucv.us.entity.Role;
import com.ucv.us.exception.ResourceNotFoundException;
import com.ucv.us.mapper.RoleMapper;
import com.ucv.us.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Autowired
    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    // Create a new role
    public RoleDTO createRole(RoleDTO roleDTO) {
        Role role = roleMapper.toEntity(roleDTO);
        Role savedRole = roleRepository.save(role);
        return roleMapper.toDto(savedRole);
    }

    // Fetch all roles
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(roleMapper::toDto)
                .collect(Collectors.toList());
    }

    // Fetch role by ID
    public RoleDTO getRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + id));
        return roleMapper.toDto(role);
    }
}

