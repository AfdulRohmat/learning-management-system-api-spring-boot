package com.afdul.belajar.springboot.learningmanagementsystem.course.dto.response;

import com.afdul.belajar.springboot.learningmanagementsystem.course.model.CourseContent;
import com.afdul.belajar.springboot.learningmanagementsystem.user.dto.response.UserInfoResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDetailResponse {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Double discount;
    private String tags;
    private String level;
    private String videoDemo;
    private String thumbnail;
    private Double ratings;
    private Integer purchased;

    // NEEDED TO SAVE A JSON FORMAT INTO DB
    @Column(columnDefinition = "jsonb", nullable = false, updatable = true, name = "benefits")
    @Type(JsonBinaryType.class)
    @JsonRawValue
    private Object benefits;

    // NEEDED TO SAVE A JSON FORMAT INTO DB
    @Column(columnDefinition = "jsonb", nullable = false, updatable = true, name = "prerequisites")
    @Type(JsonBinaryType.class)
    @JsonRawValue
    private Object prerequisites;

    @JsonProperty("course_data")
    private List<CourseContentResponse> courseData;

    UserInfoResponse author;
}

