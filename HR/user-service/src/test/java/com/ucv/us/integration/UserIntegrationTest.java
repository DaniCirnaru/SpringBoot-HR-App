//package com.ucv.us.integration;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.ucv.us.dto.CreateUserDTO;
//import com.ucv.us.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Transactional // Rollback after each test
//public class UserIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    private ObjectMapper objectMapper = new ObjectMapper();
//
//    @BeforeEach
//    void cleanDatabase() {
//        userRepository.deleteAll(); // Clean before each test
//    }
//
//    @Test
//    void registerUser_thenFetchIt() throws Exception {
//        // 1. Register a new user
//        CreateUserDTO userDTO = new CreateUserDTO();
//        userDTO.setUsername("testuser");
//        userDTO.setEmail("testuser@example.com");
//        userDTO.setPassword("password123");
//        userDTO.setRoleId(1L); // 🔥 Make sure role with ID 1 exists!!
//
//        mockMvc.perform(post("/api/users/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(userDTO)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.username").value("testuser"))
//                .andExpect(jsonPath("$.email").value("testuser@example.com"));
//
//        // 2. Fetch all users
//        mockMvc.perform(get("/api/users/email/testuser@example.com"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.username").value("testuser"));
//    }
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void deleteUser_thenNotFound() throws Exception {
//        // 1. Register a new user first
//        CreateUserDTO userDTO = new CreateUserDTO();
//        userDTO.setUsername("deletetestuser");
//        userDTO.setEmail("deletetestuser@example.com");
//        userDTO.setPassword("password123");
//        userDTO.setRoleId(1L); // Make sure this role exists!
//
//        String response = mockMvc.perform(post("/api/users/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(userDTO)))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        // Extract user ID from response
//        Long userId = objectMapper.readTree(response).get("id").asLong();
//
//        // 2. Delete the user
//        mockMvc.perform(delete("/api/users/" + userId)  // should be DELETE not POST!
//                        .with(csrf())              // 🔥 CSRF is needed for delete
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//
//        // 3. Try to fetch deleted user -> expect 404 Not Found
//        mockMvc.perform(get("/api/users/" + userId))
//                .andExpect(status().isNotFound());
//    }
//
//}
