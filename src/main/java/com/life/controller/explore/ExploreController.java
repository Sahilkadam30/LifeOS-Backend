package com.life.controller.explore;

import java.security.Principal;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.life.dto.CommentRequest;
import com.life.service.explore.ExploreService;

@RestController
@RequestMapping("/api/explore")
@CrossOrigin(origins = "*")
public class ExploreController {

    private final ExploreService exploreService;

    public ExploreController(ExploreService exploreService) {
        this.exploreService = exploreService;
    }

    @GetMapping("/feed")
    public ResponseEntity<?> getFeed(
            @RequestParam(value = "type", defaultValue = "ALL") String type,
            Principal principal
    ) {
        String currentUsername = principal != null ? principal.getName() : null;
        return ResponseEntity.ok(exploreService.getFeed(currentUsername, type));
    }

    // ================= THOUGHT POST INTERACTIONS =================

    @PostMapping("/thought/{id}/like")
    public ResponseEntity<?> toggleThoughtLike(@PathVariable Long id, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        int totalLikes = exploreService.toggleThoughtLike(id, principal.getName());
        return ResponseEntity.ok(Map.of("likesCount", totalLikes));
    }

    @PostMapping("/thought/{id}/comment")
    public ResponseEntity<?> addThoughtComment(
            @PathVariable Long id,
            @RequestBody CommentRequest request,
            Principal principal
    ) {
        if (principal == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        return ResponseEntity.ok(
                exploreService.addThoughtComment(id, principal.getName(), request.getText())
        );
    }

    @GetMapping("/thought/{id}/comments")
    public ResponseEntity<?> getThoughtComments(@PathVariable Long id) {
        return ResponseEntity.ok(exploreService.getThoughtComments(id));
    }

    @DeleteMapping("/thought/{id}")
    public ResponseEntity<?> deleteThoughtPost(@PathVariable Long id, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        exploreService.deleteThoughtPost(id, principal.getName());
        return ResponseEntity.ok(Map.of("message", "Post deleted successfully"));
    }

    // ================= ART POST INTERACTIONS =================

    @PostMapping("/art/{id}/like")
    public ResponseEntity<?> toggleArtLike(@PathVariable Long id, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        int totalLikes = exploreService.toggleArtLike(id, principal.getName());
        return ResponseEntity.ok(Map.of("likesCount", totalLikes));
    }

    @PostMapping("/art/{id}/comment")
    public ResponseEntity<?> addArtComment(
            @PathVariable Long id,
            @RequestBody CommentRequest request,
            Principal principal
    ) {
        if (principal == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        return ResponseEntity.ok(
                exploreService.addArtComment(id, principal.getName(), request.getText())
        );
    }

    @GetMapping("/art/{id}/comments")
    public ResponseEntity<?> getArtComments(@PathVariable Long id) {
        return ResponseEntity.ok(exploreService.getArtComments(id));
    }
}
