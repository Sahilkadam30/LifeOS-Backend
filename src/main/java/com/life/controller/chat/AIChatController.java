package com.life.controller.chat;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.life.dto.chat.ChatRequest;
import com.life.dto.chat.ChatResponse;
import com.life.entity.User;
import com.life.repository.UserRepository;
import com.life.service.chat.AIChatService;

@RestController
@RequestMapping("/api/chat")
public class AIChatController {

    @Autowired
    private AIChatService aiChatService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ChatResponse chat(
            @RequestBody ChatRequest request,
            Authentication authentication
    ) {

        String username = authentication.getName();

        User user = userRepository
                .findByUsername(username)
                .orElseThrow();

        String response =
                aiChatService.askGemini(
                        request.getMessage(),
                        user.getId()
                );

        return new ChatResponse(response);
    }
}
