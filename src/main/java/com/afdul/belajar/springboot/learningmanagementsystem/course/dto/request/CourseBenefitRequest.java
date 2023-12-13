package com.afdul.belajar.springboot.learningmanagementsystem.course.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseBenefitRequest {
    private String title;
}
