package com.afdul.belajar.springboot.learningmanagementsystem.course.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseContentRequest {
    private String title;
    private String description;
    private int video_length;
    private MultipartFile video_content_filename;
    private MultipartFile thumbnail;
    private Long courseId;
}
