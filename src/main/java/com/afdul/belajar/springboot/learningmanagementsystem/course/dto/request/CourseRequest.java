package com.afdul.belajar.springboot.learningmanagementsystem.course.dto.request;

import com.afdul.belajar.springboot.learningmanagementsystem.user.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseRequest {

    private String name;
    private String description;
    private double price;
    private double discount;
    private String tags;
    private String level;
    private String video_demo;
    private String thumbnail;
    private List<String> benefits;
    private List<String> prerequisites;
    private double ratings;
    private int purchased;
    private User createdBy;
}
