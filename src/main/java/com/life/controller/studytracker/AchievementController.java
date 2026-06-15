package com.life.controller.studytracker;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.life.entity.studytracker.Achievement;
import com.life.service.studytracker.AchievementService;

@RestController
@RequestMapping("/api/skills/achievements")
public class AchievementController {

    private final AchievementService service;

    public AchievementController(
            AchievementService service) {

        this.service = service;
    }

    @PostMapping
    public Achievement create(
            @RequestBody Achievement achievement) {

        return service.create(achievement);
    }

    @GetMapping
    public List<Achievement> getAll() {

        return service.getAll();
    }

    @GetMapping("/{id}")
    public Achievement getById(
            @PathVariable Long id) {

        return service.getById(id);
    }

    @PutMapping("/{id}")
    public Achievement update(
            @PathVariable Long id,
            @RequestBody Achievement achievement) {

        return service.update(id, achievement);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id) {

        service.delete(id);
    }
}
