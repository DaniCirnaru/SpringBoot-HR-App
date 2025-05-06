package com.ucv.us.service;

import com.ucv.us.dto.RoleDTO;
import com.ucv.us.entity.Role;
import com.ucv.us.exception.ResourceNotFoundException;
import com.ucv.us.mapper.RoleMapper;
import com.ucv.us.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {
    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private RoleService roleService;
    private Role role;
    private RoleDTO roleDTO;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1L);
        role.setName("ADMIN");

        roleDTO = new RoleDTO();
        roleDTO.setId(1L);
        roleDTO.setName("ADMIN");
    }

    @Test
    void testGetRoleById_Success() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(roleMapper.toDto(role)).thenReturn(roleDTO);

        RoleDTO result = roleService.getRoleById(1L);

        assertNotNull(result);
        assertEquals("ADMIN", result.getName());
    }

    @Test
    void testGetRoleById_NotFound_ThrowsException() {
        when(roleRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> roleService.getRoleById(2L));
    }

    @Test
    void testGetAllRoles_Success() {
        when(roleRepository.findAll()).thenReturn(List.of(role));
        when(roleMapper.toDto(role)).thenReturn(roleDTO);

        List<RoleDTO> roles = roleService.getAllRoles();

        assertFalse(roles.isEmpty());
        assertEquals(1, roles.size());
        assertEquals("ADMIN", roles.get(0).getName());
    }
}


