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

import com.life.entity.studytracker.StudySession;
import com.life.service.studytracker.StudySessionService;

@RestController
@RequestMapping("/api/skills/study-sessions")
public class StudySessionController {

    private final StudySessionService service;

    public StudySessionController(
            StudySessionService service) {

        this.service = service;
    }

    @PostMapping
    public StudySession create(
            @RequestBody StudySession session) {

        return service.createSession(session);
    }

    @GetMapping
    public List<StudySession> getAll() {

        return service.getAllSessions();
    }

    @GetMapping("/{id}")
    public StudySession getById(
            @PathVariable Long id) {

        return service.getSessionById(id);
    }

    @PutMapping("/{id}")
    public StudySession update(
            @PathVariable Long id,
            @RequestBody StudySession session) {

        return service.updateSession(id, session);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id) {

        service.deleteSession(id);
    }
}
