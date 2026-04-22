package com.ngulik.resto_app.controller;

import com.ngulik.resto_app.dto.UserDto;
import com.ngulik.resto_app.entity.User;
import com.ngulik.resto_app.request.RegisterRequest;
import com.ngulik.resto_app.response.ApiResponse;
import com.ngulik.resto_app.response.RegisterResponse;
import com.ngulik.resto_app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ApiAuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@Valid @RequestBody RegisterRequest request) {
        UserDto userDto = new UserDto();
        userDto.setName(request.getName());
        userDto.setEmail(request.getEmail());
        userDto.setPassword(request.getPassword());

        User registeredUser = userService.registerUser(userDto);

        RegisterResponse responseData = RegisterResponse.builder()
                .id(registeredUser.getId())
                .name(registeredUser.getName())
                .email(registeredUser.getEmail())
                .role(registeredUser.getRole())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully", responseData));
    }
}
