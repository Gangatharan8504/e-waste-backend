package com.Internship.project.controller;

import com.Internship.project.dto.*;
import com.Internship.project.entity.User;
import com.Internship.project.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return ResponseEntity.ok(userService.getProfileFromToken(email));
    }

    @PutMapping("/profile")
    public ResponseEntity<User> updateProfile(
            @RequestBody UpdateProfileRequest request) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return ResponseEntity.ok(
                userService.updateProfileFromToken(email, request)
        );
    }

    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse> changePassword(
            @RequestBody ChangePasswordRequest request) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        ApiResponse response = userService.changePassword(email, request);

        return ResponseEntity.ok(response);
    }
}