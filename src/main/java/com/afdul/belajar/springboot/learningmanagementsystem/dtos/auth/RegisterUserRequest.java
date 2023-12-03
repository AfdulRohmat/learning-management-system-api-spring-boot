package com.afdul.belajar.springboot.learningmanagementsystem.dtos.auth;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterUserRequest {

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 100)
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank
    @Size(max = 100)
    private String password;
}
