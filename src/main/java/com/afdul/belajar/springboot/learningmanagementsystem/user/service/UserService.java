package com.afdul.belajar.springboot.learningmanagementsystem.user.service;

import com.afdul.belajar.springboot.learningmanagementsystem.auth.config.security.services.UserDetailsImpl;
import com.afdul.belajar.springboot.learningmanagementsystem.user.dto.response.PurchasedCourseResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.user.dto.response.UserResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.user.model.Role;
import com.afdul.belajar.springboot.learningmanagementsystem.user.model.User;
import com.afdul.belajar.springboot.learningmanagementsystem.user.repository.UserRepository;
import jakarta.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    // GET ALL USERS -- ONLY ADMIN
    @Transactional
    public Page<UserResponse> getAllUser(String search, Pageable pageable) {
        return userRepository.getUsers(search, pageable);
    }

    // GET USER THAT CURRENTLY LOGIN
    @Transactional
    public UserResponse getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        UUID userId = userDetails.getId();

        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found !"));

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .isVerified(user.getIsVerified())
                .roles(user.getRoles().stream().map(Role::getName).toList())
                .build();
    }

    @Transactional
    public List<PurchasedCourseResponse> getPurchasedCoursesAndUserDataByUserId() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(userDetails.getId()).orElseThrow(() -> new RuntimeException("User not found"));

        List<Tuple> data = userRepository.getPurchasedCoursesAndUserDataByUserId(user.getId());


        List<PurchasedCourseResponse> purchasedCourseResponses = data.stream().map(t -> {

                    User author = userRepository.findById(t.get(5, UUID.class)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found !"));

                    UserResponse userResponse = UserResponse.builder()
                            .id(author.getId())
                            .username(author.getUsername())
                            .email(author.getEmail())
                            .build();

                    return PurchasedCourseResponse.builder()
                            .id(t.get(0, Long.class))
                            .courseId(t.get(1, Long.class))
                            .name(t.get(2, String.class))
                            .thumbnail(t.get(3, String.class))
                            .description(t.get(4, String.class))
                            .author(userResponse)
                            .build();
                })

                .toList();

        return purchasedCourseResponses;
    }

}
