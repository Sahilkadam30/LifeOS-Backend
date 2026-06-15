package com.life.controller.traveltrack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication; 
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.life.entity.User;
import com.life.entity.traveltrack.TravelPost;
import com.life.service.traveltrack.FileService;
import com.life.service.traveltrack.TravelService;

@RestController
@RequestMapping("/api/travel")
public class TravelController {

    @Autowired
    private TravelService service;

    @Autowired
    private FileService fileService; 

    @PostMapping("/post")
    public ResponseEntity<?> createPost(
            @RequestParam("placeName") String placeName,
            @RequestParam("caption") String caption,
            @RequestParam("images") List<MultipartFile> files,
            Authentication auth
    ) throws IOException {

        String username = auth.getName(); 

        User user = service.getUserByUsername(username); 

        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            String url = fileService.uploadFile(file);
            imageUrls.add(url);
        }

        TravelPost post = service.createPost(placeName, caption, user, imageUrls);

        return ResponseEntity.ok(post);
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyPosts(Authentication auth) {

        String username = auth.getName(); 

        User user = service.getUserByUsername(username); 

        return ResponseEntity.ok(service.getUserPosts(user.getId()));
    }
    
    @PostMapping("/like/{postId}")
    public ResponseEntity<?> like(@PathVariable Long postId, Authentication auth) {
        User user = service.getUserByUsername(auth.getName());
        long count = service.likePost(postId, user);
        return ResponseEntity.ok(count);
    }

    @PostMapping("/comment/{postId}")
    public ResponseEntity<?> comment(
            @PathVariable Long postId,
            @RequestBody String text,
            Authentication auth) {

        User user = service.getUserByUsername(auth.getName());
        return ResponseEntity.ok(service.addComment(postId, text, user));
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<?> delete(@PathVariable Long postId, Authentication auth) {
        User user = service.getUserByUsername(auth.getName());
        service.deletePost(postId, user);
        return ResponseEntity.ok("Deleted");
    }
    
}