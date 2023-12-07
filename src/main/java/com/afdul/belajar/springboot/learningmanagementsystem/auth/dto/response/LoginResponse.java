package com.afdul.belajar.springboot.learningmanagementsystem.auth.dto.response;

import com.afdul.belajar.springboot.learningmanagementsystem.user.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {


    private UUID id;

    @NotBlank
    private String username;

    @NotBlank
    @Email()
    private String email;

    @NotBlank
    private List<String> roles;

}