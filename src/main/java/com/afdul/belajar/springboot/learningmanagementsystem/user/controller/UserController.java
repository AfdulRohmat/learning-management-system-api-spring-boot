package com.afdul.belajar.springboot.learningmanagementsystem.user.controller;

import com.afdul.belajar.springboot.learningmanagementsystem.user.dto.response.PurchasedCourseResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.user.dto.response.UserResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.user.service.UserService;
import com.afdul.belajar.springboot.learningmanagementsystem.utils.PaginationUtil;
import com.afdul.belajar.springboot.learningmanagementsystem.utils.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/admin/all-user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> getAllUser(@RequestParam(name = "search", defaultValue = "") String search, Pageable pageable) {
        try {
            Page<UserResponse> response = userService.getAllUser(search, pageable);

            return ResponseHandler.generateResponse("Success get data", HttpStatus.OK, response);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    // GET USER THAT CURRENTLY LOGIN
    @GetMapping("/")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> getUser() {
        try {
            UserResponse response = userService.getUser();

            return ResponseHandler.generateResponse("Success get data", HttpStatus.OK, response);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }


    @GetMapping("/purchased-course")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Object> getPurchasedCoursesAndUserDataByUserId(Pageable pageable) {
        try {
            List<PurchasedCourseResponse> response = userService.getPurchasedCoursesAndUserDataByUserId();

            Page<PurchasedCourseResponse> page = PaginationUtil.paginateList(pageable, response);
            return ResponseHandler.generateResponse("Success get data", HttpStatus.OK, page);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }

}
