package com.afdul.belajar.springboot.learningmanagementsystem.controllers.auth;

import com.afdul.belajar.springboot.learningmanagementsystem.dtos.auth.RegisterUserRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.dtos.auth.RegisterUserResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.repositories.user.UserRepository;
import com.afdul.belajar.springboot.learningmanagementsystem.services.auth.AuthService;
import com.afdul.belajar.springboot.learningmanagementsystem.utils.response.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/api/v1/auth")
public class AuthController {

    @Autowired
    AuthService authService;


    @PostMapping(path = "/register")
    public ResponseEntity<Object> register(@RequestBody RegisterUserRequest request) {
        try {
            // Check if email already exist or not
            if (authService.doesUserExistByEmail(request.getEmail())) {
                return ResponseHandler.generateResponse("Email already exist", HttpStatus.BAD_REQUEST, null);
            }

            RegisterUserResponse RegisterUserResponse = authService.register(request);
            return ResponseHandler.generateResponse("Success. Please check your email to activate your account", HttpStatus.OK, RegisterUserResponse);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }
}
