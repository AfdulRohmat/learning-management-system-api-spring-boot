package com.afdul.belajar.springboot.learningmanagementsystem.auth.repository;

import com.afdul.belajar.springboot.learningmanagementsystem.auth.model.RefreshToken;
import com.afdul.belajar.springboot.learningmanagementsystem.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    void deleteByToken(String token);

    @Modifying
    void deleteByUser(User user);

}
