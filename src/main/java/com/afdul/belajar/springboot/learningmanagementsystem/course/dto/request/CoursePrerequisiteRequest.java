package com.afdul.belajar.springboot.learningmanagementsystem.course.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoursePrerequisiteRequest {
    private String title;
}
