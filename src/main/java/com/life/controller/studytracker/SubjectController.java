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

import com.life.entity.studytracker.Subject;
import com.life.service.studytracker.SubjectService;

@RestController
@RequestMapping("/api/skills/subjects")
public class SubjectController {

    private final SubjectService service;

    public SubjectController(
            SubjectService service) {

        this.service = service;
    }

    @PostMapping
    public Subject create(
            @RequestBody Subject subject) {

        return service.create(subject);
    }

    @GetMapping
    public List<Subject> getAll() {

        return service.getAll();
    }

    @GetMapping("/{id}")
    public Subject getById(
            @PathVariable Long id) {

        return service.getById(id);
    }

    @PutMapping("/{id}")
    public Subject update(
            @PathVariable Long id,
            @RequestBody Subject subject) {

        return service.update(id, subject);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id) {

        service.delete(id);
    }
}