package com.afdul.belajar.springboot.learningmanagementsystem.user.repository;

import com.afdul.belajar.springboot.learningmanagementsystem.course.model.Course;
import com.afdul.belajar.springboot.learningmanagementsystem.order.model.EStatus;
import com.afdul.belajar.springboot.learningmanagementsystem.order.model.OrderItem;
import com.afdul.belajar.springboot.learningmanagementsystem.user.dto.response.PurchasedCourseResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.user.dto.response.UserResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.user.model.User;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findFirstByEmail(String email);

    Optional<User> findFirstByEmailAndActivationCode(String email, int code);

    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    @Query("SELECT u.isVerified FROM User u WHERE u.email = :email")
    boolean isUserVerified(@Param("email") String email);


    @Query("SELECT DISTINCT u FROM User u " +
            "LEFT JOIN FETCH u.roles ur " +
            "WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<UserResponse> getUsers(@Param("search") String search, Pageable pageable);


    @Query(value = "SELECT oi.id AS id, " +
            "       c.id AS courseId, " +
            "       c.name AS name, " +
            "       c.thumbnail_url AS thumbnail, " +
            "       c.description AS description, " +
            "       c.user_id AS authorId " +
            "FROM order_items oi " +
            "LEFT JOIN users u ON u.id = oi.purchased_by " +
            "LEFT JOIN courses c ON c.id = oi.course_id " +
            "WHERE oi.status = 'SUCCESS' AND u.id = :userId " +
            "GROUP BY oi.id, c.id, c.name, c.thumbnail_url, c.description, c.user_id",
            nativeQuery = true)
    List<Tuple> getPurchasedCoursesAndUserDataByUserId(UUID userId);


}
