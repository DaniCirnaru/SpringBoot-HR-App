package com.ucv.us.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserDTO {

    private String username;

    @Email(message = "Invalid email")
    private String email;

    private String password;
}
