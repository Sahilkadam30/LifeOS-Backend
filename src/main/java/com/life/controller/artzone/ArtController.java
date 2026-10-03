package com.life.controller.artzone;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.life.dto.CommentRequest;
import com.life.entity.artzone.ArtPost;
import com.life.repository.artzone.ArtPostRepository;
import com.life.service.artzone.ArtService;

@RestController
@RequestMapping("/api/art")
@CrossOrigin(origins = "*")
public class ArtController {

	@Autowired
    private ArtService service;
	
	@Autowired
    private ArtPostRepository repo;

    @PostMapping("/post")
    public ResponseEntity<?> createPost(
            @RequestParam("file") MultipartFile file,
            @RequestParam("caption") String caption,
            @RequestParam(value = "isPublic", defaultValue = "false") boolean isPublic,
            Principal principal
    ) throws Exception {
        return ResponseEntity.ok(service.createPost(principal.getName(), file, caption, isPublic));
    }

    @GetMapping("/posts")
    public ResponseEntity<?> getPosts(Principal principal) {
        return ResponseEntity.ok(service.getPosts(principal.getName()));
    }

    @PutMapping("/toggle-visibility/{id}")
    public ResponseEntity<?> toggleVisibility(@PathVariable Long id, Principal principal) {
        try {
            ArtPost updated = service.toggleVisibility(id, principal.getName());
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    @PostMapping("/like/{id}")
    public ResponseEntity<?> likePost(@PathVariable Long id, Principal principal) {
        int totalLikes = service.likePost(id, principal.getName());
        return ResponseEntity.ok(totalLikes);
    }

    @PostMapping("/comment/{id}")
    public ResponseEntity<?> addComment(@PathVariable Long id,
                                        @RequestBody CommentRequest request,
                                        Principal principal) {
        return ResponseEntity.ok(
            service.addComment(id, principal.getName(), request.getText())
        );
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<?> getComments(@PathVariable Long id) {
        return ResponseEntity.ok(service.getComments(id));
    }
    
    @DeleteMapping("/post/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id, Principal principal) {
        ArtPost post = repo.findById(id).orElseThrow();

        if (!post.getUsername().equals(principal.getName())) {
            return ResponseEntity.status(403).body("Not allowed");
        }

        repo.delete(post);
        return ResponseEntity.ok("Deleted");
    }
}
