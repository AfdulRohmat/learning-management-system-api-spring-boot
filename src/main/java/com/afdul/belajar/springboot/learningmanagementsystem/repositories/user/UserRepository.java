package com.afdul.belajar.springboot.learningmanagementsystem.repositories.user;

import com.afdul.belajar.springboot.learningmanagementsystem.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findFirstByEmail(String email);

    boolean existsByEmail(String email);
}
