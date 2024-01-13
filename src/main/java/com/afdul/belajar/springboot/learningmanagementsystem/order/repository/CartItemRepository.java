package com.afdul.belajar.springboot.learningmanagementsystem.order.repository;

import com.afdul.belajar.springboot.learningmanagementsystem.course.model.Course;
import com.afdul.belajar.springboot.learningmanagementsystem.order.model.CartItem;
import com.afdul.belajar.springboot.learningmanagementsystem.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    long countByCourseAndUser(Course course, User user);

    Optional<List<CartItem>> findByUser(User user);
}
