package com.ucv.us.service;

import com.ucv.us.dto.CreateUserDTO;
import com.ucv.us.dto.UserDTO;
import com.ucv.us.entity.Role;
import com.ucv.us.entity.User;

import com.ucv.us.exception.DuplicateEmailException;
import com.ucv.us.exception.ResourceNotFoundException;
import com.ucv.us.mapper.UserMapper;
import com.ucv.us.repository.RoleRepository;
import com.ucv.us.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private CreateUserDTO createUserDTO;
    private User user;
    private Role role;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        createUserDTO = new CreateUserDTO();
        createUserDTO.setUsername("testUser");
        createUserDTO.setEmail("test@example.com");
        createUserDTO.setPassword("password123");
        createUserDTO.setRoleId(1L);

        role = new Role();
        role.setId(1L);
        role.setName("USER");

        user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setRole(role);

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("testUser");
        userDTO.setEmail("test@example.com");
    }

    @Test
    void registerUser_Success() {
        when(userRepository.existsByEmail(createUserDTO.getEmail())).thenReturn(false);
        when(roleRepository.findById(createUserDTO.getRoleId())).thenReturn(Optional.of(role));
        when(userMapper.toEntity(createUserDTO, role)).thenReturn(user);
        when(passwordEncoder.encode(createUserDTO.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDTO);

        UserDTO result = userService.registerUser(createUserDTO);

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        verify(userRepository).save(user);
    }

    @Test
    void registerUser_DuplicateEmail_ThrowsException() {
        when(userRepository.existsByEmail(createUserDTO.getEmail())).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> userService.registerUser(createUserDTO));
    }

    @Test
    void registerUser_RoleNotFound_ThrowsException() {
        when(userRepository.existsByEmail(createUserDTO.getEmail())).thenReturn(false);
        when(roleRepository.findById(createUserDTO.getRoleId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.registerUser(createUserDTO));
    }

    @Test
    void getAllUsers_Success() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toDto(user)).thenReturn(userDTO);

        List<UserDTO> users = userService.getAllUsers();

        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
    }

    @Test
    void getUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDTO);

        UserDTO result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
    }

    @Test
    void getUserById_NotFound_ThrowsException() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(2L));
    }

    @Test
    void getUserByEmail_Success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDTO);

        UserDTO result = userService.getUserByEmail("test@example.com");

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
    }

    @Test
    void getUserByEmail_NotFound_ThrowsException() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserByEmail("notfound@example.com"));
    }

    @Test
    void deleteUser_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_NotFound_ThrowsException() {
        when(userRepository.existsById(2L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(2L));
    }
}
