package com.ucv.us.mapper;


import com.ucv.us.dto.RoleDTO;
import com.ucv.us.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {
    public RoleDTO toDto(Role role) {
        if (role == null) {
            return null;
        }
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());
        return roleDTO;
    }

    public Role toEntity(RoleDTO roleDTO) {
        if (roleDTO == null) {
            return null;
        }
        Role role = new Role();
        role.setId(roleDTO.getId());
        role.setName(roleDTO.getName());
        return role;
    }
}
