package com.afdul.belajar.springboot.learningmanagementsystem.user.dto.response;

import com.afdul.belajar.springboot.learningmanagementsystem.course.model.Course;
import com.afdul.belajar.springboot.learningmanagementsystem.user.model.ERole;
import com.afdul.belajar.springboot.learningmanagementsystem.user.model.Role;
import com.afdul.belajar.springboot.learningmanagementsystem.user.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private UUID id;
    private String username;
    private String email;

    @JsonProperty("is_verified")
    private Boolean isVerified;

    private List<ERole> roles;

//    public UserResponse(User user) {
//        this.id = user.getId();
//        this.username = user.getUsername();
//        this.email = user.getEmail();
//        this.isVerified = user.getIsVerified();
//        this.roles = user.getRoles().stream()
//                .map(Role::getName)
//                .collect(Collectors.toList());
//
//    }
}