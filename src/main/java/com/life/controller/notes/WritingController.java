package com.life.controller.notes;

import java.util.List;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.life.dto.WritingRequest;
import com.life.entity.notes.Writing;
import com.life.entity.notes.WritingType;
import com.life.service.notes.WritingService;

@RestController
@RequestMapping("/api/writings")
@CrossOrigin(origins = "*")
public class WritingController {

	private final WritingService writingService;
	
	public WritingController(WritingService writingService) {
	    this.writingService = writingService;
	}

    @PostMapping
    public Writing create(
            @RequestBody WritingRequest request,
            @RequestHeader("userId") Long userId
    ) {
        return writingService.create(request, userId);
    }

    @GetMapping
    public List<Writing> getAll(
            @RequestHeader("userId") Long userId
    ) {
        return writingService.getAll(userId);
    }

    @GetMapping("/type/{type}")
    public List<Writing> getByType(
            @PathVariable WritingType type,
            @RequestHeader("userId") Long userId
    ) {
        return writingService.getByType(userId, type);
    }

    @PutMapping("/{id}")
    public Writing update(
            @PathVariable Long id,
            @RequestBody WritingRequest request,
            @RequestHeader("userId") Long userId
    ) {
        return writingService.update(id, request, userId);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id,
            @RequestHeader("userId") Long userId
    ) {
        writingService.delete(id, userId);
    }
    
    @PutMapping("/favorite/{id}")
    public Writing toggleFavorite(
            @PathVariable Long id,
            @RequestHeader("userId") Long userId
    ) {
        return writingService.toggleFavorite(id, userId);
    }
}
