package com.afdul.belajar.springboot.learningmanagementsystem.course.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseContentRequest {
    private String name;
    private String description;
    private int video_length;
    private String video_url;
    private String thumbnail;
    private Long courseId;
}
