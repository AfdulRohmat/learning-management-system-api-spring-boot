package com.afdul.belajar.springboot.learningmanagementsystem.course.controller;

import com.afdul.belajar.springboot.learningmanagementsystem.course.dto.request.CourseContentRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.course.dto.request.CourseRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.question.dto.request.QuestionRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.course.dto.response.CourseDetailResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.course.dto.response.CoursePreviewResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.course.service.CourseService;
import com.afdul.belajar.springboot.learningmanagementsystem.question.dto.request.ReviewRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.utils.ResponseHandler;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/course")
public class CourseController {

    @Autowired
    CourseService courseService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> createCourse(@Valid CourseRequest courseRequest) {
        try {
            courseService.createCourse(courseRequest);

            return ResponseHandler.generateResponse("Success create course", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/content",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> createCourseContent(@Valid CourseContentRequest request) {
        try {
            courseService.createCourseContent(request);

            return ResponseHandler.generateResponse("Success create course content", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    // GET ALL COURSES WITHOUT PURCHASE
    @GetMapping("/without-purchase/")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<Object> searchCoursesWithoutPurchase(@RequestParam(name = "search", required = false, defaultValue = "") String search,
                                                               @PageableDefault(page = 0, size = 10) Pageable pageable) {
        try {
            Page<CoursePreviewResponse> response = courseService.getAllCourses(search, pageable);

            return ResponseHandler.generateResponse("Success get data", HttpStatus.OK, response);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    // GET DETAIL COURSE WITHOUT PURCHASE
    @GetMapping("/without-purchase/{courseId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<Object> getCourseWithoutPurchase(@PathVariable Long courseId) {
        try {
            CourseDetailResponse response = courseService.getCourseWithoutPurchase(courseId);

            return ResponseHandler.generateResponse("Success get data", HttpStatus.OK, response);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    // GET DETAIL COURSE AFTER PURCHASE / ADMIN
    @GetMapping("/{courseId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') ")
    public ResponseEntity<Object> getCourseAfterPurchase(@PathVariable Long courseId) {
        try {
            CourseDetailResponse response = courseService.getCourseAfterPurchase(courseId);

            return ResponseHandler.generateResponse("Success get data", HttpStatus.OK, response);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    // ADD REVIEW TO COURSE
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/review",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> addReview(@Valid ReviewRequest request) {
        try {
            courseService.addReview(request);

            return ResponseHandler.generateResponse("Success create course", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }


}
