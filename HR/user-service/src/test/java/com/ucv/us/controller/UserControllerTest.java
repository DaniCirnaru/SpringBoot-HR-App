//package com.ucv.us.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.ucv.us.config.TestSecurityConfig;
//import com.ucv.us.dto.CreateUserDTO;
//import com.ucv.us.dto.RoleDTO;
//import com.ucv.us.dto.UserDTO;
//import com.ucv.us.service.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(controllers = UserController.class)
//@Import(TestSecurityConfig.class)
//class UserControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private UserService userService;
//
//    private UserDTO userDTO;
//    private CreateUserDTO createUserDTO;
//
//    @BeforeEach
//    void setUp() {
//        RoleDTO roleDTO = new RoleDTO();
//        roleDTO.setId(1L);
//        roleDTO.setName("USER");
//
//        userDTO = new UserDTO();
//        userDTO.setId(1L);
//        userDTO.setUsername("john_doe");
//        userDTO.setEmail("john@example.com");
//        userDTO.setRole(roleDTO);
//
//        createUserDTO = new CreateUserDTO();
//        createUserDTO.setUsername("john_doe");
//        createUserDTO.setEmail("john@example.com");
//        createUserDTO.setPassword("password123");
//        createUserDTO.setRoleId(1L);
//    }
//
//    @Test
//    @WithMockUser(username = "testuser")
//    void registerUser_ReturnsCreatedUser() throws Exception {
//        when(userService.registerUser(any(CreateUserDTO.class))).thenReturn(userDTO);
//
//        mockMvc.perform(post("/api/users/register")
//
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(createUserDTO)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.username").value("john_doe"));
//    }
//
//
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void getAllUsers_ReturnsListOfUsers() throws Exception {
//        when(userService.getAllUsers()).thenReturn(List.of(userDTO));
//
//        mockMvc.perform(get("/api/users"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].username").value("john_doe"));
//    }
//
//    @Test
//    @WithMockUser(roles = "USER")
//    void getUserById_ReturnsUser() throws Exception {
//        when(userService.getUserById(1L)).thenReturn(userDTO);
//
//        mockMvc.perform(get("/api/users/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.username").value("john_doe"));
//    }
//
//    @Test
//    @WithMockUser
//    void getUserByEmail_ReturnsUser() throws Exception {
//        when(userService.getUserByEmail("john@example.com")).thenReturn(userDTO);
//
//        mockMvc.perform(get("/api/users/email/john@example.com"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.username").value("john_doe"));
//    }
//
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void deleteUser_ReturnsSuccessMessage() throws Exception {
//        mockMvc.perform(delete("/api/users/1"))
//                // <<< ADD THIS
//                .andExpect(status().isOk())
//                .andExpect(content().string("User deleted successfully"));
//    }
//}
