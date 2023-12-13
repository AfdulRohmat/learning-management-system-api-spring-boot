package com.afdul.belajar.springboot.learningmanagementsystem.course.dto.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseDataRequest {
    private String title;
    private String description;
    private String videoUrl;
    private int videoLength;
}
