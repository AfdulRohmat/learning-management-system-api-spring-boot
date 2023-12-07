package com.afdul.belajar.springboot.learningmanagementsystem.user.repository;

import com.afdul.belajar.springboot.learningmanagementsystem.user.model.User;
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

}
