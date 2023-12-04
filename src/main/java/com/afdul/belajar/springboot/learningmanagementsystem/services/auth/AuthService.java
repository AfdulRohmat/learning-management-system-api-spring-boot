package com.afdul.belajar.springboot.learningmanagementsystem.services.auth;

import com.afdul.belajar.springboot.learningmanagementsystem.dtos.auth.RegisterUserRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.dtos.auth.RegisterUserResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.dtos.auth.VerifyEmailRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.models.user.User;
import com.afdul.belajar.springboot.learningmanagementsystem.repositories.user.UserRepository;
import com.afdul.belajar.springboot.learningmanagementsystem.services.email.EmailService;
import com.afdul.belajar.springboot.learningmanagementsystem.services.validation.ValidationService;
import com.afdul.belajar.springboot.learningmanagementsystem.utils.response.ResponseHandler;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static com.afdul.belajar.springboot.learningmanagementsystem.utils.ActivationCodeGenerator.generateActivationCode;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ValidationService validationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;


    // REGISTER
    @Transactional
    public RegisterUserResponse register(RegisterUserRequest request) {
        validationService.validate(request);

        // generate bcrypt password
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        // generate activation code
        int activationCode = generateActivationCode();

        // Define User instance, then set new value
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(hashedPassword);
        user.setActivationCode(activationCode);
        user.setRole("user");
        user.setIsVerified(false);

        // Send email function
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("To confirm your account, use this code: " + user.getActivationCode());
        emailService.sendEmail(mailMessage);

        // save user
        userRepository.save(user);

        return RegisterUserResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public boolean doesUserExistByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // VERIFY EMAIL
    @Transactional
    public void verifyEmail(VerifyEmailRequest request) {
        validationService.validate(request);

        // Check if user by email exist. If not throw error
        // Check the activationCode is valid or not. If not throw error
        User user = userRepository.findFirstByEmailAndActivationCode(request.getEmail(), request.getActivationCode()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid email or code"));

        // Check if email already verified. If true throw error
        if (user.getIsVerified()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
        }

        // Set isVerified to true
        user.setIsVerified(true);

        // Save new Data
        userRepository.save(user);
    }


}
