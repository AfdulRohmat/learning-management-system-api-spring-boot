package com.afdul.belajar.springboot.learningmanagementsystem.course.dto.response;

import com.afdul.belajar.springboot.learningmanagementsystem.course.model.Course;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseContentResponse {
    private Long id;
    private String title;
    private String description;
    private int video_length;
    private String video_url;
    private String thumbnail;
}
