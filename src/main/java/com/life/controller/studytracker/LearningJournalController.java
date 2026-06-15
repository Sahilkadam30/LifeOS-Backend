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

import com.life.entity.studytracker.LearningJournal;
import com.life.service.studytracker.LearningJournalService;

@RestController
@RequestMapping("/api/skills/journals")
public class LearningJournalController {

    private final LearningJournalService service;

    public LearningJournalController(
            LearningJournalService service) {

        this.service = service;
    }

    @PostMapping
    public LearningJournal create(
            @RequestBody LearningJournal journal) {

        return service.createJournal(journal);
    }

    @GetMapping
    public List<LearningJournal> getAll() {

        return service.getAllJournals();
    }

    @GetMapping("/{id}")
    public LearningJournal getById(
            @PathVariable Long id) {

        return service.getJournalById(id);
    }

    @PutMapping("/{id}")
    public LearningJournal update(
            @PathVariable Long id,
            @RequestBody LearningJournal journal) {

        return service.updateJournal(id, journal);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id) {

        service.deleteJournal(id);
    }
}
