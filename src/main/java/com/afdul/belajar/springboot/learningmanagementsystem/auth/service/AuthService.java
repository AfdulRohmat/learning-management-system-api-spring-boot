package com.afdul.belajar.springboot.learningmanagementsystem.auth.service;

import com.afdul.belajar.springboot.learningmanagementsystem.auth.config.security.services.UserDetailsImpl;
import com.afdul.belajar.springboot.learningmanagementsystem.auth.config.security.jwt.JwtUtils;
import com.afdul.belajar.springboot.learningmanagementsystem.auth.dto.request.*;
import com.afdul.belajar.springboot.learningmanagementsystem.auth.dto.response.LoginResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.auth.dto.response.LogoutResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.auth.dto.response.RefreshTokenResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.auth.dto.response.RegisterUserResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.auth.exception.TokenRefreshException;
import com.afdul.belajar.springboot.learningmanagementsystem.auth.model.RefreshToken;
import com.afdul.belajar.springboot.learningmanagementsystem.auth.repository.RefreshTokenRepository;
import com.afdul.belajar.springboot.learningmanagementsystem.user.model.ERole;
import com.afdul.belajar.springboot.learningmanagementsystem.user.model.Role;
import com.afdul.belajar.springboot.learningmanagementsystem.user.model.User;
import com.afdul.belajar.springboot.learningmanagementsystem.user.repository.RoleRepository;
import com.afdul.belajar.springboot.learningmanagementsystem.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.*;

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

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private EntityManager entityManager;


    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${app.jwtCookieName}")
    private String jwtCookieName;


    // LOGIN
    @Transactional
    public LoginResponse login(LoginRequest request, HttpServletResponse response) {

        // Check if User by Email exist. if not throw error
        userRepository.findFirstByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("User not found. Please register first"));

        // Check if is_verified == true. if False throw error
        boolean isVerified = userRepository.isUserVerified(request.getEmail());
        if (!isVerified) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account not verified, please verify your account before");
        }

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        if (authentication.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            String jwt = jwtUtils.generateJwtToken(userDetails);

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            // RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

//
// ============= IMPLEMENTATION IF USING COOKIE
//            String accessToken = jwtUtils.generateJwtToken(authentication);

//            ResponseCookie cookie = ResponseCookie.from(jwtCookieName, jwt)
//                    .httpOnly(true)
//                    .secure(false)
//                    .path("/")
//                    .maxAge(jwtExpirationMs)
//                    .build();
//
//            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            return LoginResponse.builder()
                    .username(userDetails.getUsername())
                    .email(userDetails.getEmail())
                    .roles(roles)
                    .accessToken(jwt)
                    .refreshToken("")
                    .tokenType("Bearer")
                    .build();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email or password");
        }

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
    public LogoutResponse logoutUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID userId = userDetails.getId();
        refreshTokenService.deleteByUserId(userId);


//        ============= IMPLEMENTATION IF USING COOKIE
//        ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
//        ResponseCookie jwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie();

        return new LogoutResponse("Success logout");
    }


    // REFRESH TOKEN
    @Transactional
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {

        return refreshTokenService.findByToken(request.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = jwtUtils.generateTokenFromUsername(user.getEmail());
                    return RefreshTokenResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(request.getRefreshToken())
                            .build();
                }).orElseThrow(() -> new RuntimeException(
                        "Refresh token is not in database!"));


//        String requestRefreshToken = request.getRefreshToken();
//
//        // Get Token by Id
//        RefreshToken token = refreshTokenRepository.findByToken(requestRefreshToken).orElseThrow(() -> new RuntimeException("Refresh Token is not in DB..!!"));
//
//        // Verify expiration by compare a date
//        // If Expired, delete token then throw error. ask to login again
//        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
//            User user = userRepository.findById(token.getUser().getId()).orElseThrow(() -> new RuntimeException("User not found"));
//            refreshTokenRepository.deleteByUser(user);
//            System.out.println("Success Delete ");
//            throw new RuntimeException("Refresh token was expired. Please make a new login request");
//        }
//
//        // generate new refresh token
//        User user = userRepository.findById(token.getUser().getId()).orElseThrow(() -> new RuntimeException("User not found"));
//        String accessToken = jwtUtils.generateTokenFromUsername(user.getEmail());
//        return RefreshTokenResponse.builder()
//                .accessToken(accessToken)
//                .refreshToken(requestRefreshToken)
//                .build();


    }


}
