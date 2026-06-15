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

import com.life.entity.studytracker.SkillProgress;
import com.life.service.studytracker.SkillProgressService;

@RestController
@RequestMapping("/api/skills/progress")
public class SkillProgressController {

    private final SkillProgressService service;

    public SkillProgressController(
            SkillProgressService service) {

        this.service = service;
    }

    @PostMapping
    public SkillProgress create(
            @RequestBody SkillProgress progress) {

        return service.create(progress);
    }

    @GetMapping
    public List<SkillProgress> getAll() {

        return service.getAll();
    }

    @GetMapping("/{id}")
    public SkillProgress getById(
            @PathVariable Long id) {

        return service.getById(id);
    }

    @PutMapping("/{id}")
    public SkillProgress update(
            @PathVariable Long id,
            @RequestBody SkillProgress progress) {

        return service.update(id, progress);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id) {

        service.delete(id);
    }
}
