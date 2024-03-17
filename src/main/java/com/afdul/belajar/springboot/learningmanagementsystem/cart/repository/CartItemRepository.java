package com.afdul.belajar.springboot.learningmanagementsystem.cart.repository;

import com.afdul.belajar.springboot.learningmanagementsystem.course.model.Course;
import com.afdul.belajar.springboot.learningmanagementsystem.cart.model.CartItem;
import com.afdul.belajar.springboot.learningmanagementsystem.user.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    long countByCourseAndUser(Course course, User user);


    List<CartItem> findByUser(User user, Pageable pageable);

    @Query("SELECT SUM(ci.course.price) FROM CartItem ci WHERE ci.user = :user")
    Optional<Double> getTotalPrice(@Param("user") User user);

    Integer countByUser(User user);

    @Modifying
    @Query("DELETE FROM CartItem")
    void deleteAll();


}
