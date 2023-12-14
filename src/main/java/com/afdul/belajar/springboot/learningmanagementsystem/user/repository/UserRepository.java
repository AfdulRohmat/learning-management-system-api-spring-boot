package com.afdul.belajar.springboot.learningmanagementsystem.user.repository;

import com.afdul.belajar.springboot.learningmanagementsystem.course.dto.response.CoursesWithoutPurchase;
import com.afdul.belajar.springboot.learningmanagementsystem.user.dto.response.UserResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

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


}
