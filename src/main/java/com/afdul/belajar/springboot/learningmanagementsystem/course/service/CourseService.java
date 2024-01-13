package com.afdul.belajar.springboot.learningmanagementsystem.course.service;

import com.afdul.belajar.springboot.learningmanagementsystem.auth.config.security.services.UserDetailsImpl;
import com.afdul.belajar.springboot.learningmanagementsystem.course.dto.request.CourseContentRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.course.dto.request.CourseRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.course.model.Review;
import com.afdul.belajar.springboot.learningmanagementsystem.course.repository.ReviewRepository;
import com.afdul.belajar.springboot.learningmanagementsystem.course.dto.response.*;
import com.afdul.belajar.springboot.learningmanagementsystem.course.model.Course;
import com.afdul.belajar.springboot.learningmanagementsystem.course.model.CourseContent;
import com.afdul.belajar.springboot.learningmanagementsystem.course.dto.request.ReviewRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.course.repository.*;
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
public class CourseService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseContentRepository courseContentRepository;
    @Autowired
    private ReviewRepository reviewRepository;


    // CREATE A COURSE -- ONLY ADMIN
    @Transactional
    public void createCourse(CourseRequest courseRequest) {

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(userDetails.getId()).orElseThrow(() -> new RuntimeException("User not found"));

        // Map Course DTO
        Course course = new Course();
        course.setName(courseRequest.getName());
        course.setDescription(courseRequest.getDescription());
        course.setPrice(courseRequest.getPrice());
        course.setDiscount(courseRequest.getDiscount());
        course.setTags(courseRequest.getTags());
        course.setLevel(courseRequest.getLevel());
        course.setVideoDemo(courseRequest.getVideo_demo());
        course.setThumbnail(courseRequest.getThumbnail());
        course.setBenefits(courseRequest.getBenefits());
        course.setPrerequisites(courseRequest.getPrerequisites());
        course.setRatings(0);
        course.setPurchased(0);
        course.setCreatedBy(user);

        courseRepository.save(course);
    }

    @Transactional
    public void createCourseContent(CourseContentRequest request) {
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(() -> new RuntimeException("Course not found"));

        // Map Course DTO
        CourseContent courseContent = new CourseContent();
        courseContent.setTitle(request.getTitle());
        courseContent.setDescription(request.getDescription());
        courseContent.setVideo_length(request.getVideo_length());
        courseContent.setVideo_url(request.getVideo_url());
        courseContent.setThumbnail(request.getThumbnail());
        courseContent.setCourseId(course);

        courseContentRepository.save(courseContent);
    }


    // GET ALL COURSES --WITHOUT PURCHASE
    @Transactional
    public Page<CoursePreviewResponse> getAllCourses(String search, Pageable pageable) {
        return courseRepository.getAllCourses(search, pageable);
    }

    // GET DETAIL COURSES --WITHOUT PURCHASE
    @Transactional
    public CourseDetailResponse getCourseWithoutPurchase(Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));

        return CourseDetailResponse.builder()
                .id(course.getId())
                .name(course.getName())
                .description(course.getDescription())
                .price(course.getPrice())
                .discount(course.getDiscount())
                .tags(course.getTags())
                .level(course.getLevel())
                .videoDemo(course.getVideoDemo())
                .thumbnail(course.getThumbnail())
                .ratings(course.getRatings())
                .purchased(course.getPurchased())
                .benefits(course.getBenefits())
                .prerequisites(course.getPrerequisites())
                .author(mapToUserInfoToDTOs(course.getCreatedBy()))
                .build();
    }

    // GET FULL DETAIL COURSE AFTER PURCHASE / ADMIN
    public CourseDetailResponse getCourseAfterPurchase(Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));

        return CourseDetailResponse.builder()
                .id(course.getId())
                .name(course.getName())
                .description(course.getDescription())
                .price(course.getPrice())
                .discount(course.getDiscount())
                .tags(course.getTags())
                .level(course.getLevel())
                .videoDemo(course.getVideoDemo())
                .thumbnail(course.getThumbnail())
                .ratings(course.getRatings())
                .purchased(course.getPurchased())
                .benefits(course.getBenefits())
                .prerequisites(course.getPrerequisites())
                .courseData(mapCourseContentsToDTOs(course.getContents()))
                .author(mapToUserInfoToDTOs(course.getCreatedBy()))
                .build();
    }

    // EXAMPLE HOW MAP OBJECT
    private UserInfoResponse mapToUserInfoToDTOs(User user) {
        return UserInfoResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
    }

    // EXAMPLE HOW TO MAP LIST / ARRAY
    private List<CourseContentResponse> mapCourseContentsToDTOs(List<CourseContent> courseContents) {
        return courseContents.stream()
                .map(courseContent -> CourseContentResponse.builder()
                        .id(courseContent.getId())
                        .title(courseContent.getTitle())
                        .description(courseContent.getDescription())
                        .video_url(courseContent.getVideo_url())
                        .video_length(courseContent.getVideo_length())
                        .thumbnail(courseContent.getThumbnail())
                        .build()
                )
                .collect(Collectors.toList());
    }

    // ADD REVIEW TO COURSE
    @Transactional
    public void addReview(ReviewRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(userDetails.getId()).orElseThrow(() -> new RuntimeException("User not found"));
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(() -> new RuntimeException("Course not found"));
        List<Review> reviews = reviewRepository.findByCourseId(course);

        int totalRatings = 0;
        double averageRating = 0.0;
        if (reviews.isEmpty()) {
            averageRating = 0.0;
        }

        for (Review review : reviews) {
            totalRatings += review.getRating();
        }

        averageRating = (double) totalRatings / reviews.size();

        System.out.println("ratings :" + averageRating);

        Review review = new Review();

        review.setReview(request.getReview());
        review.setRating(request.getRating());
        review.setCourseId(course);
        review.setCreatedBy(user);

        reviewRepository.save(review);

        course.setRatings(averageRating);

    }

    // GET REVIEW BY COURSE ID
    @Transactional
    public Page<ReviewResponse> getAllReviewByCourseId(Long courseId, Pageable pageable) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        List<Review> reviews = reviewRepository.getAllReviewByCourseId(course, pageable);

        return new PageImpl<>(reviews.stream()
                .map(review -> mapToReviewResponse(review, course))
                .collect(Collectors.toList()), pageable, reviews.size());

    }

    private ReviewResponse mapToReviewResponse(Review review, Course course) {
        UserInfoResponse userInfoResponse = new UserInfoResponse(
                review.getCreatedBy().getId(),
                review.getCreatedBy().getUsername(),
                review.getCreatedBy().getEmail()
        );

        return ReviewResponse.builder()
                .id(review.getId())
                .review(review.getReview())
                .rating(review.getRating())
                .user(userInfoResponse)
                .createdAt(review.getCreatedAt())
                .build();
    }


}
