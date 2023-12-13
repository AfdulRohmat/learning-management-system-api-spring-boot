package com.afdul.belajar.springboot.learningmanagementsystem.course.repository;

import com.afdul.belajar.springboot.learningmanagementsystem.course.model.CourseData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseDataRepository extends JpaRepository<CourseData, Long> {
}
