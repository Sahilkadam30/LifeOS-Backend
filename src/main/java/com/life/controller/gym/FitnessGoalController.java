package com.life.controller.gym;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.life.entity.gym.FitnessGoal;
import com.life.service.gym.FitnessGoalService;

@RestController
@RequestMapping("/api/gym/goals")
@CrossOrigin("*")
public class FitnessGoalController {

	private final FitnessGoalService service;

    public FitnessGoalController(
            FitnessGoalService service) {
        this.service = service;
    }

    @PostMapping
    public FitnessGoal create(
            @RequestBody FitnessGoal goal) {

        return service.create(goal);
    }

    @GetMapping
    public List<FitnessGoal> getAll() {

        return service.getAll();
    }

    @GetMapping("/{id}")
    public FitnessGoal getById(
            @PathVariable Long id) {

        return service.getById(id);
    }

    @PutMapping("/{id}")
    public FitnessGoal update(
            @PathVariable Long id,
            @RequestBody FitnessGoal goal) {

        return service.update(id, goal);
    }

    @PatchMapping("/{id}/progress")
    public FitnessGoal updateProgress(
            @PathVariable Long id,
            @RequestBody Map<String, Double> body) {

        return service.updateProgress(
                id,
                body.get("progress")
        );
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id) {

        service.delete(id);
    }
}
