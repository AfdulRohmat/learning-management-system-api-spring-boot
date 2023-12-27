package com.afdul.belajar.springboot.learningmanagementsystem.user.dto.response;


import com.afdul.belajar.springboot.learningmanagementsystem.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoResponse {
    private UUID id;
    private String username;
    private String email;

    public static UserInfoResponse fromUser(User user) {
        return UserInfoResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}
