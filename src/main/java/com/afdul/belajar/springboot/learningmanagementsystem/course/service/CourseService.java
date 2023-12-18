package com.afdul.belajar.springboot.learningmanagementsystem.course.service;

import com.afdul.belajar.springboot.learningmanagementsystem.auth.config.security.services.UserDetailsImpl;
import com.afdul.belajar.springboot.learningmanagementsystem.course.dto.request.CourseContentRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.course.dto.request.CourseRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.course.dto.response.CoursesWithoutPurchase;
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

//        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        User user = userRepository.findById(userDetails.getId()).orElseThrow(() -> new RuntimeException("User not found"));

        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(() -> new RuntimeException("Course not found"));

        // Map Course DTO
        CourseContent courseContent = new CourseContent();
        courseContent.setName(request.getName());
        courseContent.setDescription(request.getDescription());
        courseContent.setVideo_length(request.getVideo_length());
        courseContent.setVideo_url(request.getVideo_url());
        courseContent.setThumbnail(request.getThumbnail());
        courseContent.setCourse_id(course);

        courseContentRepository.save(courseContent);
    }


    // GET ALL COURSES WITHOUT PURCHASE
//    @Transactional
//    public Page<CoursesWithoutPurchase> getCoursesWithoutPurchase(String search, Pageable pageable) {
//        return courseRepository.searchCoursesWithoutPurchase(search, pageable);
//    }

    // GET COURSE DETAIL WITHOUT PURCHASE
//    @Transactional
//    public CoursesWithoutPurchase getCourseDetailWithoutPurchase(Long courseId) {
//        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException(
//                "Course not found"));
//
//        return new CoursesWithoutPurchase(
//                course.getId(),
//                course.getName(),
//                course.getDescription(),
//                course.getPrice(),
//                course.getEstimatedPrice(),
//                course.getTags(),
//                course.getLevel(),
//                course.getDemoUrl(),
//                course.getBenefits(),
//                course.getPrerequisites()
//        );
//    }
}
