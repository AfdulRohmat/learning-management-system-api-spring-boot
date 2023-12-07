package com.afdul.belajar.springboot.learningmanagementsystem.auth.controller;

import com.afdul.belajar.springboot.learningmanagementsystem.auth.config.security.jwt.JwtUtils;
import com.afdul.belajar.springboot.learningmanagementsystem.auth.dto.request.LoginRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.auth.dto.request.RegisterUserRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.auth.dto.request.ResendCodeRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.auth.dto.response.RegisterUserResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.auth.dto.request.VerifyEmailRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.auth.service.AuthService;
import com.afdul.belajar.springboot.learningmanagementsystem.user.repository.RoleRepository;
import com.afdul.belajar.springboot.learningmanagementsystem.user.repository.UserRepository;
import com.afdul.belajar.springboot.learningmanagementsystem.utils.ResponseHandler;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "/api/v1/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthService authService;


    @PostMapping(path = "/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterUserRequest request) {
        try {
            // Check if email already exist or not
            if (authService.doesUserExistByEmail(request.getEmail())) {
                return ResponseHandler.generateResponse("Email already exist", HttpStatus.BAD_REQUEST, null);
            }

            RegisterUserResponse registerUserResponse = authService.register(request);
            return ResponseHandler.generateResponse("Success. Please check your email to activate your account", HttpStatus.OK, registerUserResponse);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @PostMapping(path = "/verify-email")
    public ResponseEntity<Object> verifyEmail(@RequestBody VerifyEmailRequest request) {
        try {
            authService.verifyEmail(request);
            return ResponseHandler.generateResponse("Activation Success. Please login", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @PostMapping(path = "/resend-activation-code")
    public ResponseEntity<Object> resendActivationCode(@RequestBody ResendCodeRequest request) {
        try {
            authService.resendActivationCode(request);
            return ResponseHandler.generateResponse("Success. Please check your email to activate your account", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest request) {
        try {

            ResponseEntity<?> loginResponse = authService.login(request);
            return ResponseHandler.generateResponse("Success login", HttpStatus.OK, loginResponse);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout() {
        try {
            authService.logoutUser();
            return ResponseHandler.generateResponse("Success to logout", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }


}
