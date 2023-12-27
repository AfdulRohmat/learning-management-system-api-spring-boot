package com.afdul.belajar.springboot.learningmanagementsystem.course.repository;

import com.afdul.belajar.springboot.learningmanagementsystem.course.dto.response.CoursePreviewResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.course.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT new com.afdul.belajar.springboot.learningmanagementsystem.course.dto.response.CoursePreviewResponse(" +
            "c.id, c.name, c.price, " +
            "c.thumbnail, c.ratings, c.purchased, " +
            "new com.afdul.belajar.springboot.learningmanagementsystem.user.dto.response.UserInfoResponse(c.createdBy.id, c.createdBy.username, c.createdBy.email))" +
            "FROM Course c " +
            "WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<CoursePreviewResponse> getAllCourses(@Param("search") String search, Pageable pageable);
}


