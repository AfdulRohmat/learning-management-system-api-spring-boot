package com.afdul.belajar.springboot.learningmanagementsystem.auth.service;

import com.afdul.belajar.springboot.learningmanagementsystem.auth.config.security.services.UserDetailsImpl;
import com.afdul.belajar.springboot.learningmanagementsystem.auth.config.security.jwt.JwtUtils;
import com.afdul.belajar.springboot.learningmanagementsystem.auth.dto.request.LoginRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.auth.dto.request.RegisterUserRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.auth.dto.request.ResendCodeRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.auth.dto.response.LoginResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.auth.dto.response.RegisterUserResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.auth.dto.request.VerifyEmailRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.user.model.ERole;
import com.afdul.belajar.springboot.learningmanagementsystem.user.model.Role;
import com.afdul.belajar.springboot.learningmanagementsystem.user.model.User;
import com.afdul.belajar.springboot.learningmanagementsystem.user.repository.RoleRepository;
import com.afdul.belajar.springboot.learningmanagementsystem.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.afdul.belajar.springboot.learningmanagementsystem.auth.util.ActivationCodeGenerator.generateActivationCode;

@Service
public class AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    ValidationService validationService;

    @Autowired
    EmailService emailService;

    @Autowired
    PasswordEncoder encoder;

    // LOGIN
    @Transactional
    public ResponseEntity<?> login(LoginRequest request) {

        // Check if is_verified == true. if False throw error
        boolean isVerified = userRepository.isUserVerified(request.getEmail());
        if(!isVerified){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account not verified, please verify your account before");
        }

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new LoginResponse(
                        userDetails.getId(), userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles)
                );
    }


    // REGISTER
    @Transactional
    public RegisterUserResponse register(RegisterUserRequest request) {
        validationService.validate(request);

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already registered");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
        }


        // generate bcrypt password
        String hashedPassword = encoder.encode(request.getPassword());

        // generate activation code
        int activationCode = generateActivationCode();

        // Define User instance, then set new value
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(hashedPassword);
        user.setActivationCode(activationCode);
        user.setIsVerified(false);

        // Set Role
        Set<String> strRoles = request.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                if (role.equals("admin")) {
                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(adminRole);
                } else if (role.equals("user")) {
                    Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(userRole);
                }

            });
        }

        user.setRoles(roles);

        // Send email function
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("To confirm your account, use this code: " + user.getActivationCode());
        emailService.sendEmail(mailMessage);

        // save user
        userRepository.save(user);

        return RegisterUserResponse.builder()
                .name(user.getUsername())
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

    // RESEND ACTIVATION CODE
    @Transactional
    public void resendActivationCode(ResendCodeRequest request) {
        validationService.validate(request);

        // Check user by Email, if not exist throw error
        User user = userRepository.findFirstByEmail(request.getEmail()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found !"));

        // Delete Activation Code
        user.setActivationCode(null);

        // Generate new activation code
        int newActivationCode = generateActivationCode();
        user.setActivationCode(newActivationCode);

        // Sending Email activation code
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("To confirm your account, use this code: " + user.getActivationCode());
        emailService.sendEmail(mailMessage);

        // Save new activation code
        userRepository.save(user);
    }

    // LOGOUT
    @Transactional
    public ResponseCookie logoutUser() {
        return jwtUtils.getCleanJwtCookie();
    }


}