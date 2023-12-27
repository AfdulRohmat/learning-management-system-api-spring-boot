package com.afdul.belajar.springboot.learningmanagementsystem.question.service;


import com.afdul.belajar.springboot.learningmanagementsystem.auth.config.security.services.UserDetailsImpl;
import com.afdul.belajar.springboot.learningmanagementsystem.course.dto.response.CourseContentResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.course.dto.response.CoursePreviewResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.course.model.Course;
import com.afdul.belajar.springboot.learningmanagementsystem.course.model.CourseContent;
import com.afdul.belajar.springboot.learningmanagementsystem.course.repository.CourseContentRepository;
import com.afdul.belajar.springboot.learningmanagementsystem.course.repository.CourseRepository;
import com.afdul.belajar.springboot.learningmanagementsystem.question.dto.request.QuestionReplyRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.question.dto.request.QuestionRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.question.dto.response.QuestionReplyResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.question.dto.response.QuestionResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.question.model.Question;
import com.afdul.belajar.springboot.learningmanagementsystem.question.model.QuestionReply;
import com.afdul.belajar.springboot.learningmanagementsystem.question.repository.QuestionReplyRepository;
import com.afdul.belajar.springboot.learningmanagementsystem.question.repository.QuestionRepository;
import com.afdul.belajar.springboot.learningmanagementsystem.user.dto.response.UserInfoResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.user.model.User;
import com.afdul.belajar.springboot.learningmanagementsystem.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseContentRepository courseContentRepository;
    @Autowired
    private QuestionReplyRepository questionReplyRepository;


    // CREATE A QUESTION ON SPECIFIC COURSE AND COURSE CONTENT
    @Transactional
    public void createQuestion(QuestionRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userRepository.findById(userDetails.getId()).orElseThrow(() -> new RuntimeException("User not found"));
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(() -> new RuntimeException("Course not found"));
        CourseContent courseContent = courseContentRepository.findById(request.getCourseContentId()).orElseThrow(() -> new RuntimeException("Course Content not found"));

        Question question = new Question();
        question.setQuestion(request.getQuestion());
        question.setCourseId(course);
        question.setCourseContentId(courseContent);
        question.setCreatedBy(user);

        questionRepository.save(question);
    }

    // GET ALL QUESTIONS BASED ON COURSE ID AND COURSE DATA ID
    @Transactional
    public Page<QuestionResponse> getAllQuestions(Long courseId, Long courseContentId, Pageable pageable) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        CourseContent courseContent = courseContentRepository.findById(courseContentId).orElseThrow(() -> new RuntimeException("Course content not found"));

        List<Question> questions = questionRepository.getAllQuestions(course, courseContent, pageable);

        return new PageImpl<>(questions.stream()
                .map(question -> mapToQuestionResponse(course, courseContent, question))
                .collect(Collectors.toList()), pageable, questions.size());
    }

    private QuestionResponse mapToQuestionResponse(Course course, CourseContent courseContent, Question question) {
        long replyCount = questionRepository.countRepliesByQuestionId(course, courseContent, question);

        UserInfoResponse userInfoResponse = new UserInfoResponse(
                question.getCreatedBy().getId(),
                question.getCreatedBy().getUsername(),
                question.getCreatedBy().getEmail()
        );

        return QuestionResponse.builder()
                .id(question.getId())
                .question(question.getQuestion())
                .user(userInfoResponse)
                .replies(replyCount)
                .build();
    }


    // GET DETAIL QUESTION BY ID, INCLUDE LIST OF REPLIES
    @Transactional
    public QuestionResponse getQuestion(QuestionReplyRequest request) {
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(() -> new RuntimeException("Course not found"));
        CourseContent courseContent = courseContentRepository.findById(request.getCourseContentId()).orElseThrow(() -> new RuntimeException("Course content not found"));
        Question question = questionRepository.findByIdAndCourseIdAndCourseContentId(request.getQuestionId(), course, courseContent).orElseThrow(() -> new RuntimeException("Question not found"));

        return QuestionResponse.builder()
                .id(question.getId())
                .question(question.getQuestion())
                .user(mapToUserInfoToDTOs(course.getCreatedBy()))
                .replies(mapQuestionReplyToDTOs(question.getQuestionReplies()))
                .build();
    }

    private UserInfoResponse mapToUserInfoToDTOs(User user) {
        return UserInfoResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
    }

    private List<QuestionReplyResponse> mapQuestionReplyToDTOs(List<QuestionReply> questionReplies) {
        return questionReplies.stream()
                .map(questionReply -> QuestionReplyResponse.builder()
                        .id(questionReply.getId())
                        .reply(questionReply.getReply())
                        .user(mapToUserInfoToDTOs(questionReply.getCreatedBy()))
                        .build()
                )
                .collect(Collectors.toList());
    }


    // CREATE A QUESTION REPLY
    @Transactional
    public void addQuestionReply(QuestionReplyRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userRepository.findById(userDetails.getId()).orElseThrow(() -> new RuntimeException("User not found"));
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(() -> new RuntimeException("Course not found"));
        CourseContent courseContent = courseContentRepository.findById(request.getCourseContentId()).orElseThrow(() -> new RuntimeException("Course Content not found"));
        Question question = questionRepository.findById(request.getQuestionId()).orElseThrow(() -> new RuntimeException("Question not found"));

        QuestionReply questionReply = new QuestionReply();
        questionReply.setReply(request.getReply());
        questionReply.setQuestionId(question);
        questionReply.setCourseId(course);
        questionReply.setCourseContentId(courseContent);
        questionReply.setCreatedBy(user);

        questionReplyRepository.save(questionReply);
    }
}
