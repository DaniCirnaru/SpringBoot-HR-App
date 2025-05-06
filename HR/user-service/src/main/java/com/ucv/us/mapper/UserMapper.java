package com.ucv.us.mapper;

import com.ucv.us.dto.UserDTO;
import com.ucv.us.dto.CreateUserDTO;
import com.ucv.us.entity.User;
import com.ucv.us.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final RoleMapper roleMapper;

    @Autowired
    public UserMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }
    public UserDTO toDto(User user) {
        if (user == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setRole(roleMapper.toDto(user.getRole()));
        return userDTO;
    }

    public User toEntity(CreateUserDTO createUserDTO, Role role) {
        if (createUserDTO == null) {
            return null;
        }
        User user = new User();
        user.setUsername(createUserDTO.getUsername());
        user.setEmail(createUserDTO.getEmail());
        user.setPassword(createUserDTO.getPassword()); // Password hashing handled in the service
        user.setRole(role);
        return user;
    }
}
