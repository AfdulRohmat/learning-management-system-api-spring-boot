package com.afdul.belajar.springboot.learningmanagementsystem.course.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoursesWithoutPurchase {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Double estimatedPrice;
    private String tags;
    private String level;
    private String demoUrl;
    private List<String> benefits;
    private List<String> prerequisites;

    // Custom constructor
    public CoursesWithoutPurchase(Long id, String name, String description, Double price, Double estimatedPrice, String tags, String level, String demoUrl, String benefits, String prerequisites) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.estimatedPrice = estimatedPrice;
        this.tags = tags;
        this.level = level;
        this.demoUrl = demoUrl;
        this.benefits = Collections.singletonList(benefits);
        this.prerequisites = Collections.singletonList(prerequisites);
    }
}
