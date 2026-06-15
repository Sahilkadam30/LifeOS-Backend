package com.life.controller.planner;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.life.entity.planner.PlannerTask;
import com.life.service.planner.PlannerTaskService;

@RestController
@RequestMapping("/api/planner/dashboard")
public class PlannerTaskController {

    private final PlannerTaskService service;

    public PlannerTaskController(
            PlannerTaskService service) {

        this.service = service;
    }

    @PostMapping
    public PlannerTask createTask(
            @RequestBody PlannerTask task) {

        return service.createTask(task);
    }

    @GetMapping
    public List<PlannerTask> getAllTasks() {

        return service.getAllTasks();
    }

    @GetMapping("/{id}")
    public PlannerTask getTaskById(
            @PathVariable Long id) {

        return service.getTaskById(id);
    }

    @PutMapping("/{id}")
    public PlannerTask updateTask(
            @PathVariable Long id,
            @RequestBody PlannerTask task) {

        return service.updateTask(
                id,
                task
        );
    }

    @PutMapping("/{id}/complete")
    public PlannerTask markCompleted(
            @PathVariable Long id) {

        return service.markCompleted(id);
    }

    @GetMapping("/upcoming")
    public List<PlannerTask> upcomingTasks() {

        return service.getUpcomingTasks();
    }

    @DeleteMapping("/{id}")
    public void deleteTask(
            @PathVariable Long id) {

        service.deleteTask(id);
    }
}
