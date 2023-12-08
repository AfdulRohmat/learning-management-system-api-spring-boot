package com.afdul.belajar.springboot.learningmanagementsystem.auth.config.advice;

import lombok.*;

import java.util.Objects;


@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorMessage {
    private int status;
    private String message;
    private String data;

}
