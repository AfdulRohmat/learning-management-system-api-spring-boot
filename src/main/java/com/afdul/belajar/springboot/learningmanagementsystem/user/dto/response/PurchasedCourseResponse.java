package com.afdul.belajar.springboot.learningmanagementsystem.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchasedCourseResponse {
    private Long id;
    private Long courseId;
    private String name;
    private String thumbnail;
    private String description;
    private UserResponse author;
}
