package com.afdul.belajar.springboot.learningmanagementsystem.services.auth;

import com.afdul.belajar.springboot.learningmanagementsystem.dtos.auth.RegisterUserRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.dtos.auth.RegisterUserResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.models.user.User;
import com.afdul.belajar.springboot.learningmanagementsystem.repositories.user.UserRepository;
import com.afdul.belajar.springboot.learningmanagementsystem.services.validation.ValidationService;
import com.afdul.belajar.springboot.learningmanagementsystem.utils.response.ResponseHandler;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.afdul.belajar.springboot.learningmanagementsystem.utils.ActivationCodeGenerator.generateActivationCode;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ValidationService validationService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Transactional
    public RegisterUserResponse register(RegisterUserRequest request) {
        validationService.validate(request);

        // generate bcrypt password
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        // generate activsation code
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
}
