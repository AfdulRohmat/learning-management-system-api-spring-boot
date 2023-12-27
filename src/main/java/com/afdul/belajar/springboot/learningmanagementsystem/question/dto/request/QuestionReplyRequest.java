package com.afdul.belajar.springboot.learningmanagementsystem.question.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionReplyRequest {
    private String reply;
    private Long courseId;
    private Long courseContentId;
    private Long questionId;
}
