package com.ucv.us.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ucv.us.config.TestSecurityConfig;
import com.ucv.us.controller.UserController;
import com.ucv.us.dto.CreateUserDTO;
import com.ucv.us.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(TestSecurityConfig.class)
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    @WithMockUser(roles = "USER")
    void handleResourceNotFound_Returns404() throws Exception {
        when(userService.getUserById(99L))
                .thenThrow(new ResourceNotFoundException("User not found with ID: 99"));

        assertErrorResponse(
                mockMvc.perform(get("/api/users/99")),
                404,
                "User not found with ID: 99",
                "Not Found",
                "/api/users/99"
        );
    }

    @Test
    @WithMockUser(roles = "USER")
    void handleDuplicateEmail_Returns400() throws Exception {
        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setUsername("john_doe");
        createUserDTO.setEmail("john@example.com");
        createUserDTO.setPassword("password123");
        createUserDTO.setRoleId(1L);

        when(userService.registerUser(any(CreateUserDTO.class)))
                .thenThrow(new DuplicateEmailException("Email is already in use"));

        assertErrorResponse(
                mockMvc.perform(post("/api/users/register")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(createUserDTO))),
                400,
                "Email is already in use",
                "Bad Request",
                "/api/users/register"
        );
    }


    private void assertErrorResponse(org.springframework.test.web.servlet.ResultActions resultActions,
                                     int expectedStatus,
                                     String expectedMessage,
                                     String expectedError,
                                     String expectedPath) throws Exception {
        resultActions
                .andExpect(status().is(expectedStatus))
                .andExpect(jsonPath("$.status").value(expectedStatus))
                .andExpect(jsonPath("$.message").value(expectedMessage))
                .andExpect(jsonPath("$.error").value(expectedError))
                .andExpect(jsonPath("$.path").value(expectedPath));
    }

}
