package com.Internship.project.controller;

import com.Internship.project.dto.*;
import com.Internship.project.entity.User;
import com.Internship.project.security.JwtUtil;
import com.Internship.project.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // ================= REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<User> register(
            @Valid @RequestBody RegisterRequest request) {

        User user = userService.register(request);
        return ResponseEntity.ok(user);
    }

    // ================= VERIFY OTP =================
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request) {

        userService.verifyOtp(request.getEmail(), request.getOtp());
        return ResponseEntity.ok("Email verified successfully");
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @Valid @RequestBody LoginRequest request) {

        User user = userService.loginAndReturnUser(request);

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole()
        );

        return ResponseEntity.ok(token);
    }

    // ================= FORGOT PASSWORD =================
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {

        userService.forgotPassword(request);
        return ResponseEntity.ok("OTP sent to email");
    }

    // ================= RESET PASSWORD =================
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {

        userService.resetPassword(request);
        return ResponseEntity.ok("Password reset successfully");
    }

    // ================= CHANGE PASSWORD =================
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestParam String email,
            @Valid @RequestBody ChangePasswordRequest request) {

        userService.changePassword(email, request);
        return ResponseEntity.ok("Password updated successfully");
    }
}