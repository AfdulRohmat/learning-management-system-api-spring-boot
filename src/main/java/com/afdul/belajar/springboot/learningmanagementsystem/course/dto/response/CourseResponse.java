package com.afdul.belajar.springboot.learningmanagementsystem.course.dto.response;

import com.afdul.belajar.springboot.learningmanagementsystem.course.model.CourseBenefit;
import com.afdul.belajar.springboot.learningmanagementsystem.course.model.CourseData;
import com.afdul.belajar.springboot.learningmanagementsystem.course.model.CoursePrerequisite;
import com.afdul.belajar.springboot.learningmanagementsystem.user.model.User;
import jakarta.persistence.*;
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

    private List<CourseBenefit> benefits;

    private List<CoursePrerequisite> prerequisites;

    private List<CourseData> courseData;

    private User createdBy;
}
