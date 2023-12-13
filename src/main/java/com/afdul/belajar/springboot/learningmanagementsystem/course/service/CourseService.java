package com.afdul.belajar.springboot.learningmanagementsystem.course.service;

import com.afdul.belajar.springboot.learningmanagementsystem.auth.config.security.services.UserDetailsImpl;
import com.afdul.belajar.springboot.learningmanagementsystem.course.dto.request.CourseRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.course.model.Course;
import com.afdul.belajar.springboot.learningmanagementsystem.course.model.CourseBenefit;
import com.afdul.belajar.springboot.learningmanagementsystem.course.model.CourseData;
import com.afdul.belajar.springboot.learningmanagementsystem.course.model.CoursePrerequisite;
import com.afdul.belajar.springboot.learningmanagementsystem.course.repository.*;
import com.afdul.belajar.springboot.learningmanagementsystem.user.model.User;
import com.afdul.belajar.springboot.learningmanagementsystem.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    CourseBenefitRepository courseBenefitRepository;

    @Autowired
    CourseDataRepository courseDataRepository;

    @Autowired
    CoursePrerequisiteRepository coursePrerequisiteRepository;

    @Transactional
    public void createCourse(CourseRequest courseRequest,
                             List<CourseBenefit> courseBenefitRequest,
                             List<CourseData> courseDataRequest,
                             List<CoursePrerequisite> coursePrerequisitesRequest
    ) {

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(userDetails.getId()).orElseThrow(() -> new RuntimeException("User not found"));

        // Map Course DTO
        Course course = new Course();
        course.setName(courseRequest.getName());
        course.setDescription(courseRequest.getDescription());
        course.setPrice(courseRequest.getPrice());
        course.setEstimatedPrice(courseRequest.getEstimatedPrice());
        course.setTags(courseRequest.getTags());
        course.setLevel(courseRequest.getLevel());
        course.setDemoUrl(courseRequest.getDemoUrl());
        course.setCreatedBy(user);

        // Map CoursePrerequisite DTO
        List<CoursePrerequisite> prerequisites = mapCoursePrerequisiteDtosToEntities(coursePrerequisitesRequest);


        // Map CourseBenefit DTO
        List<CourseBenefit> benefits = mapBenefitDtosToEntities(courseBenefitRequest);

        // Map CourseData DTO
        List<CourseData> courseData = mapCourseDataDtosToEntities(courseDataRequest);

        // Set all Mapping to Course
        course.setPrerequisites(prerequisites);
        course.setBenefits(benefits);
        course.setCourseData(courseData);

        courseRepository.save(course);
    }

    private List<CoursePrerequisite> mapCoursePrerequisiteDtosToEntities(List<CoursePrerequisite> coursePrerequisitesRequest) {
        return coursePrerequisitesRequest.stream()
                .map(request -> {
                    CoursePrerequisite coursePrerequisite = new CoursePrerequisite();
                    coursePrerequisite.setTitle(request.getTitle());

                    return coursePrerequisite;
                }).toList();

    }

    private List<CourseData> mapCourseDataDtosToEntities(List<CourseData> courseDataRequest) {
        return courseDataRequest.stream()
                .map(request -> {
                    CourseData courseData = new CourseData();
                    courseData.setTitle(request.getTitle());
                    courseData.setDescription(request.getDescription());
                    courseData.setVideoLength(request.getVideoLength());
                    courseData.setVideoUrl(request.getVideoUrl());

                    return courseData;
                }).toList();
    }


    private List<CourseBenefit> mapBenefitDtosToEntities(List<CourseBenefit> courseBenefitRequest) {
        return courseBenefitRequest.stream()
                .map(request -> {
                    CourseBenefit courseBenefit = new CourseBenefit();
                    courseBenefit.setTitle(request.getTitle());

                    return courseBenefit;
                }).toList();
    }
}
