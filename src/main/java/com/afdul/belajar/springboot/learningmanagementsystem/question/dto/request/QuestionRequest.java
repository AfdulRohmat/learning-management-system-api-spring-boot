package com.afdul.belajar.springboot.learningmanagementsystem.question.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionRequest {
    private String question;
    private Long courseId;
    private Long courseContentId;
}
