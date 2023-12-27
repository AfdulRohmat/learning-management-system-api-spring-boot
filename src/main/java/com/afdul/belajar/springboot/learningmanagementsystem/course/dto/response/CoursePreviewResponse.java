package com.afdul.belajar.springboot.learningmanagementsystem.course.dto.response;

import com.afdul.belajar.springboot.learningmanagementsystem.user.dto.response.UserInfoResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoursePreviewResponse {
    private Long id;
    private String name;
    private Double price;
    private String thumbnail;
    private Double ratings;
    private Integer purchased;
    private UserInfoResponse author;

}
