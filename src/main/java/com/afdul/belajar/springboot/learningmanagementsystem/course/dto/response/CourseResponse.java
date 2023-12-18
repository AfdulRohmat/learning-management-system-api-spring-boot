package com.afdul.belajar.springboot.learningmanagementsystem.course.dto.response;

import com.afdul.belajar.springboot.learningmanagementsystem.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseResponse {
    private String name;
    private String description;

    private double price;
    private double estimatedPrice;

    private String tags;
    private String level;
    private String demoUrl;


    private User createdBy;
}
