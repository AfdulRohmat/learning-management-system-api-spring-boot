package com.afdul.belajar.springboot.learningmanagementsystem.course.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewRequest {
    private String review;

    @Min(value = 1)
    @Max(value = 5)
    private Integer rating;

    private Long courseId;
}
