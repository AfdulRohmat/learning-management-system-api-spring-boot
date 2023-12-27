package com.afdul.belajar.springboot.learningmanagementsystem.question.controller;

import com.afdul.belajar.springboot.learningmanagementsystem.course.dto.response.CoursePreviewResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.question.dto.request.QuestionReplyRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.question.dto.request.QuestionRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.question.dto.response.QuestionResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.question.service.QuestionService;
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

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/question")
public class QuestionController {

    @Autowired
    QuestionService questionService;

    // CREATE A QUESTION
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping(value = "/",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> createQuestion(@Valid QuestionRequest request) {
        try {
            questionService.createQuestion(request);

            return ResponseHandler.generateResponse("Success create a question", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }


    // GET ALL QUESTIONS BASED ON COURSE ID AND COURSE DATA ID
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/")
    public ResponseEntity<Object> getAllQuestions(@RequestParam(name = "courseId", required = true) Long courseId,
                                                  @RequestParam(name = "courseContentId", required = true) Long courseContentId,
                                                  @PageableDefault(page = 0, size = 10) Pageable pageable) {
        try {
            Page<QuestionResponse> response = questionService.getAllQuestions(courseId, courseContentId, pageable);

            return ResponseHandler.generateResponse("Success get data", HttpStatus.OK, response);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    // GET DETAIL QUESTION BY ID, INCLUDE LIST OF REPLIES
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/detail")
    public ResponseEntity<Object> getQuestion(@Valid QuestionReplyRequest request) {
        try {
            QuestionResponse response = questionService.getQuestion(request);

            return ResponseHandler.generateResponse("Success get data", HttpStatus.OK, response);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }


    // ADD QUESTION REPLY
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping(value = "/reply/",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> addQuestionReply(@Valid QuestionReplyRequest request) {
        try {
            questionService.addQuestionReply(request);

            return ResponseHandler.generateResponse("Success create adding reply", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }
}
