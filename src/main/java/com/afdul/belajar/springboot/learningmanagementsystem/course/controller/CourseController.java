package com.afdul.belajar.springboot.learningmanagementsystem.course.controller;

import com.afdul.belajar.springboot.learningmanagementsystem.course.dto.request.CourseRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.course.dto.response.CoursesWithoutPurchase;
import com.afdul.belajar.springboot.learningmanagementsystem.course.service.CourseService;
import com.afdul.belajar.springboot.learningmanagementsystem.utils.ResponseHandler;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/course")
public class CourseController {

    @Autowired
    CourseService courseService;

    @PostMapping("/")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> createCourse(@Valid @RequestBody CourseRequest courseRequest) {
        try {
            courseService.createCourse(courseRequest, courseRequest.getBenefits(), courseRequest.getCourseData(), courseRequest.getPrerequisites());

            return ResponseHandler.generateResponse("Success create course", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/without-purchase/all")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<Object> searchCoursesWithoutPurchase(@RequestParam(name = "search", defaultValue = "") String search, Pageable pageable) {
        try {
            Page<CoursesWithoutPurchase> response = courseService.searchCoursesWithoutPurchase(search, pageable);

            return ResponseHandler.generateResponse("Success get data", HttpStatus.OK, response);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }


    @GetMapping("/without-purchase/{courseId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<Object> getCourseDetailWithoutPurchase(@PathVariable Long courseId) {
        try {
            CoursesWithoutPurchase response = courseService.getCourseDetailWithoutPurchase(courseId);

            return ResponseHandler.generateResponse("Success get data", HttpStatus.OK, response);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

}
