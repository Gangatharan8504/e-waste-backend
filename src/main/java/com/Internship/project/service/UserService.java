package com.Internship.project.service;

import com.Internship.project.dto.*;
import com.Internship.project.entity.User;

public interface UserService {

    // ===== AUTH =====
    User register(RegisterRequest request);

    void verifyOtp(String email, String otp);

    User loginAndReturnUser(LoginRequest request);

    void forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);

    // ===== USER (JWT BASED) =====
    User getProfileFromToken(String email);

    User updateProfileFromToken(String email, UpdateProfileRequest request);

    ApiResponse changePassword(String email, ChangePasswordRequest request);
}