package com.afdul.belajar.springboot.learningmanagementsystem.course.repository;

import com.afdul.belajar.springboot.learningmanagementsystem.course.model.CoursePrerequisite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoursePrerequisiteRepository extends JpaRepository<CoursePrerequisite, Long> {
}
