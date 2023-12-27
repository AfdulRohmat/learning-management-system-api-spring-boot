package com.afdul.belajar.springboot.learningmanagementsystem.course.repository;

import com.afdul.belajar.springboot.learningmanagementsystem.course.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}