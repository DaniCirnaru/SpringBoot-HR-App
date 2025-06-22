package com.ucv.auth.dto;

public record UserDTO(Long id, String username, String email, RoleDTO role,String password) {}
