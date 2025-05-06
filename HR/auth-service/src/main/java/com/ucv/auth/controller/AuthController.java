package com.ucv.auth.controller;

import com.ucv.auth.client.UserServiceClient;
import com.ucv.auth.dto.AuthResponseDTO;
import com.ucv.auth.dto.LoginDTO;
import com.ucv.auth.dto.UserDTO;
import com.ucv.auth.security.JWTGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserServiceClient userServiceClient;
    private final JWTGenerator jwtGenerator;

    public AuthController(UserServiceClient userServiceClient, JWTGenerator jwtGenerator) {
        this.userServiceClient = userServiceClient;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO loginDto) {
        System.out.println("🔹 Attempting login for email: " + loginDto.email());

        UserDTO userDTO = userServiceClient.getUserByEmail(loginDto.email());

        if (userDTO == null) {
            System.out.println("❌ User not found in user-service!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        System.out.println("✅ User found: " + userDTO.username());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginDto.email(), loginDto.password());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String roleName = userDTO.role().name();
        String token = jwtGenerator.generateToken(loginDto.email(), roleName);

        return ResponseEntity.ok(new AuthResponseDTO(token));
    }
}
