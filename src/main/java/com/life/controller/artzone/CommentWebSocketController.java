package com.life.controller.artzone;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.life.dto.CommentRequest;
import com.life.entity.artzone.Comment;
import com.life.service.artzone.ArtService;

@Controller
public class CommentWebSocketController {

	@Autowired
    private ArtService service;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/comment")
    public void sendComment(CommentRequest request) {

        Comment comment = service.addComment(
            request.getPostId(),
            request.getUsername(),
            request.getText()
        );

        messagingTemplate.convertAndSend(
            "/topic/comments/" + request.getPostId(),
            comment
        );
    }
}
