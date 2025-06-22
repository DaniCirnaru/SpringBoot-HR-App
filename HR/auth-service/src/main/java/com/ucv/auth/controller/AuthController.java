package com.ucv.auth.controller;

import com.ucv.auth.client.UserServiceClient;
import com.ucv.auth.dto.AuthResponseDTO;
import com.ucv.auth.dto.LoginDTO;
import com.ucv.auth.dto.UserDTO;
import com.ucv.auth.security.JWTGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserServiceClient userServiceClient;
    private final JWTGenerator jwtGenerator;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserServiceClient userServiceClient, JWTGenerator jwtGenerator, PasswordEncoder passwordEncoder) {
        this.userServiceClient = userServiceClient;
        this.jwtGenerator = jwtGenerator;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO loginDto) {
        System.out.println("🔹 Attempting login for email: " + loginDto.email());

        UserDTO userDTO = userServiceClient.getUserByEmail(loginDto.email());

        if (userDTO == null) {
            System.out.println("❌ User not found in user-service!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        if (userDTO.role() == null) {
            throw new IllegalStateException("User role is missing in user-service response.");
        }
        System.out.println("🔐 Raw password: " + loginDto.password());
        System.out.println("🔐 Stored hash: " + userDTO.password());
        System.out.println("🔐 Matches? " + passwordEncoder.matches(loginDto.password(), userDTO.password()));

        // 🔒 Check password
        if (!passwordEncoder.matches(loginDto.password(), userDTO.password())) {
            System.out.println("❌ Invalid password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        System.out.println("✅ User authenticated: " + userDTO.username());

        String roleName = userDTO.role().name();
        String token = jwtGenerator.generateToken(loginDto.email(), roleName);

        return ResponseEntity.ok(new AuthResponseDTO(token));
    }

}
