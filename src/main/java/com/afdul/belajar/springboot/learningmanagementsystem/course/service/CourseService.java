package com.afdul.belajar.springboot.learningmanagementsystem.course.service;

import com.afdul.belajar.springboot.learningmanagementsystem.auth.config.security.services.UserDetailsImpl;
import com.afdul.belajar.springboot.learningmanagementsystem.course.dto.request.CourseContentRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.course.dto.request.CourseRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.course.dto.response.CourseContentResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.course.dto.response.CourseDetailResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.course.dto.response.CourseResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.course.model.Course;
import com.afdul.belajar.springboot.learningmanagementsystem.course.model.CourseContent;
import com.afdul.belajar.springboot.learningmanagementsystem.course.repository.*;
import com.afdul.belajar.springboot.learningmanagementsystem.user.model.User;
import com.afdul.belajar.springboot.learningmanagementsystem.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    CourseContentRepository courseContentRepository;


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
    public Page<CourseResponse> getAllCourses(String search, Pageable pageable) {
        return courseRepository.getAllCourses(search, pageable);
    }

    // GET DETAIL COURSES --WITHOUT PURCHASE
    @Transactional
    public CourseResponse getCourseWithoutPurchase(Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));

        CourseResponse courseResponse = new CourseResponse();
        courseResponse.setName(course.getName());

        return CourseResponse.builder()
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
                .build();
    }

    // GET FULL DETAIL COURSE AFTER PURCHASE / ADMIN
    public CourseDetailResponse getCourseAfterPurchase(Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        List<CourseContent> courseContents = courseContentRepository.findByCourseId(course);

        List<CourseContentResponse> courseDataList = courseContents.stream()
                .map(courseContent -> CourseContentResponse.builder()
                        .id(courseContent.getId())
                        .title(courseContent.getTitle())
                        .description(courseContent.getDescription())
                        .video_url(courseContent.getVideo_url())
                        .video_length(courseContent.getVideo_length())
                        .thumbnail(courseContent.getThumbnail())
                        .build())
                .toList();

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
                .courseData(courseDataList)
                .build();
    }

}
