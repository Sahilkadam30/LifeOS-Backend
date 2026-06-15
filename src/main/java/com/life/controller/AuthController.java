package com.life.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.life.dto.ApiResponse;
import com.life.dto.LoginResponse;
import com.life.entity.User;
import com.life.security.JwtUtil;
import com.life.service.UserService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private UserService service;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ApiResponse register(@RequestBody User user) {
        return service.register(user);
    }

    @PostMapping("/login")
    public ApiResponse login(@RequestBody User req) {

        ApiResponse response = service.login(req);

        if (!response.isSuccess()) {
            return response;
        }

        User user = (User) response.getData();

        String token = jwtUtil.generateToken(user);

        LoginResponse loginData = new LoginResponse(user, token);

        return new ApiResponse(true, "Login successful", loginData);
    }
}
