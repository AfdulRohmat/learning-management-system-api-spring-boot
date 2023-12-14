package com.afdul.belajar.springboot.learningmanagementsystem.course.repository;

import com.afdul.belajar.springboot.learningmanagementsystem.course.dto.response.CoursesWithoutPurchase;
import com.afdul.belajar.springboot.learningmanagementsystem.course.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT new com.afdul.belajar.springboot.learningmanagementsystem.course.dto.response.CoursesWithoutPurchase(c.id, c.name, c.description, c.price, c.estimatedPrice, c.tags, c.level, c.demoUrl, cb, cp) FROM Course c LEFT JOIN c.benefits cb LEFT JOIN c.prerequisites cp WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<CoursesWithoutPurchase> searchCoursesWithoutPurchase(@Param("search") String search, Pageable pageable);
}


