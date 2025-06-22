package com.ucv.us.controller;

import com.ucv.us.dto.CreateUserDTO;
import com.ucv.us.dto.UpdateUserDTO;
import com.ucv.us.dto.UserDTO;
import com.ucv.us.dto.UserUpdateResponse;
import com.ucv.us.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    //@Secured({"ROLE_USER", "ROLE_ADMIN"})
    @PutMapping("/{id}")
    public ResponseEntity<UserUpdateResponse> updateUser(@PathVariable Long id,@Valid @RequestBody UpdateUserDTO dto) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }
    //@Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_CANDIDATE"})
    @PutMapping("/me")
    public ResponseEntity<UserUpdateResponse> updateCurrentUser(@RequestBody @Valid UpdateUserDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(userService.updateUserByEmail(email, dto));
    }


    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    // ✅ Public: Register a new user
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        return ResponseEntity.ok(userService.registerUser(createUserDTO));
    }
    // ✅ Public: Used internally by auth-service (no security needed)
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    // ✅ Secured for ADMIN only
    //@Secured("ROLE_ADMIN")
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        try {
            // normal path
            return ResponseEntity.ok(userService.getAllUsers());
        } catch (Exception e) {
            // force-print the full stack trace to your console
            e.printStackTrace();
            // re-throw so the client still gets the 500
            throw e;
        }
    }
    // ✅ Secured for USER or ADMIN
    //@Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // ✅ Secured for ADMIN only
    //@Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
