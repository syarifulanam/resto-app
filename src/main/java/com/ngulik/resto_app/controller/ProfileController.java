package com.ngulik.resto_app.controller;

import com.ngulik.resto_app.dto.UserProfileDto;
import com.ngulik.resto_app.response.ApiResponse;
import com.ngulik.resto_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileDto>> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        UserProfileDto profile = userService.getUserProfile(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Profile retrieved successfully", profile));
    }
}
