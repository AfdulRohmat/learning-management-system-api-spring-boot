package com.afdul.belajar.springboot.learningmanagementsystem.course.repository;

import com.afdul.belajar.springboot.learningmanagementsystem.course.dto.response.CourseResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.course.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT new com.afdul.belajar.springboot.learningmanagementsystem.course.dto.response.CourseResponse(" +
            "c.id, c.name, c.description, c.price, " +
            "c.discount, c.tags, c.level, c.videoDemo, c.thumbnail, c.ratings, c.purchased, c.benefits, c.prerequisites) " +
            "FROM Course c " +
            "WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<CourseResponse> getAllCourses(@Param("search") String search, Pageable pageable);
}


