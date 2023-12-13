package com.afdul.belajar.springboot.learningmanagementsystem.course.controller;

import com.afdul.belajar.springboot.learningmanagementsystem.auth.dto.request.ResendCodeRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.course.dto.request.CourseBenefitRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.course.dto.request.CourseDataRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.course.dto.request.CoursePrerequisiteRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.course.dto.request.CourseRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.course.service.CourseService;
import com.afdul.belajar.springboot.learningmanagementsystem.utils.ResponseHandler;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/course")
public class CourseController {

    @Autowired
    CourseService courseService;

    @PostMapping("/")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> createCourse(
            @Valid @RequestBody CourseRequest courseRequest
    ) {
        try {
            courseService.createCourse(
                    courseRequest,
                    courseRequest.getBenefits(),
                    courseRequest.getCourseData(),
                    courseRequest.getPrerequisites()
            );

            return ResponseHandler.generateResponse("Success create course", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }
}
