package com.afdul.belajar.springboot.learningmanagementsystem.question.dto.response;

import com.afdul.belajar.springboot.learningmanagementsystem.user.dto.response.UserInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionReplyResponse {
    private Long id;
    private String reply;
    private UserInfoResponse user;
}
