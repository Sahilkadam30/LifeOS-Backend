package com.life.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.life.dto.ApiResponse;
import com.life.entity.User;
import com.life.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder encoder;

    // 🔥 REGISTER
    public ApiResponse register(User user) {

        String username = user.getUsername().trim();
        String email = user.getEmail().trim();

        if (repo.existsByUsername(username)) {
            return new ApiResponse(false, "Username already taken");
        }

        if (repo.existsByEmail(email)) {
            return new ApiResponse(false, "Email already registered");
        }

        user.setUsername(username);
        user.setEmail(email);
        user.setRole("ROLE_USER"); // 🔥 FIX HERE
        user.setPassword(encoder.encode(user.getPassword()));

        repo.save(user);

        return new ApiResponse(true, "User Registered Successfully");
    }

    // 🔥 LOGIN LOGIC
    public ApiResponse login(User req) {

        String username = req.getUsername().trim();

        User user = repo.findByUsername(username).orElse(null);

        if (user == null) {
            return new ApiResponse(false, "User not found");
        }

        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            return new ApiResponse(false, "Invalid credentials");
        }

        return new ApiResponse(true, "Login successful", user);
    }

    // (Optional - if you still want it elsewhere)
    public boolean matchPassword(String raw, String encoded) {
        return encoder.matches(raw, encoded);
    }
}
