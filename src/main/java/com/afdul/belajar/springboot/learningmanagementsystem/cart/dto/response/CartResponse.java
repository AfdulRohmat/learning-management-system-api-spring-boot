package com.afdul.belajar.springboot.learningmanagementsystem.cart.dto.response;

import com.afdul.belajar.springboot.learningmanagementsystem.course.dto.response.CoursePreviewResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponse {
    private Long id;
    private CoursePreviewResponse course;
    private int quantity;
}
