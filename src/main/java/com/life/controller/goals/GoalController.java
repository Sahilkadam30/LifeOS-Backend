package com.life.controller.goals;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.life.dto.goals.AchievementDTO;
import com.life.dto.goals.GoalProgressDTO;
import com.life.dto.goals.GoalRequestDTO;
import com.life.dto.goals.GoalStatsDTO;
import com.life.dto.goals.MilestoneRequestDTO;
import com.life.dto.goals.ProgressUpdateRequestDTO;
import com.life.entity.goals.Goal;
import com.life.entity.goals.GoalCategory;
import com.life.entity.goals.GoalMilestone;
import com.life.entity.goals.GoalProgressUpdate;
import com.life.entity.goals.GoalStatus;
import com.life.entity.goals.GoalType;
import com.life.service.goals.GoalService;

@RestController
@RequestMapping("/api/goals")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class GoalController {

    private final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @PostMapping
    public ResponseEntity<Goal> createGoal(@RequestBody GoalRequestDTO req) {
        Goal created = goalService.createGoal(req);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Goal>> getAllGoals(
            @RequestParam(required = false) GoalType type,
            @RequestParam(required = false) GoalStatus status,
            @RequestParam(required = false) GoalCategory category) {
        return ResponseEntity.ok(goalService.getAllGoals(type, status, category));
    }

    @GetMapping("/stats")
    public ResponseEntity<GoalStatsDTO> getStats() {
        return ResponseEntity.ok(goalService.getStats());
    }

    @GetMapping("/achievements")
    public ResponseEntity<List<AchievementDTO>> getAchievements() {
        return ResponseEntity.ok(goalService.getAchievements());
    }

    @GetMapping("/{id:[0-9]+}")
    public ResponseEntity<Goal> getGoalById(@PathVariable Long id) {
        return ResponseEntity.ok(goalService.getGoalById(id));
    }

    @PutMapping("/{id:[0-9]+}")
    public ResponseEntity<Goal> updateGoal(
            @PathVariable Long id,
            @RequestBody GoalRequestDTO req) {
        return ResponseEntity.ok(goalService.updateGoal(id, req));
    }

    @PatchMapping("/{id:[0-9]+}/progress")
    public ResponseEntity<Goal> updateProgress(
            @PathVariable Long id,
            @RequestBody GoalProgressDTO progressDTO) {
        return ResponseEntity.ok(goalService.updateProgress(id, progressDTO.getProgressPercentage()));
    }

    @PutMapping("/{id:[0-9]+}/complete")
    public ResponseEntity<Goal> markCompleted(@PathVariable Long id) {
        return ResponseEntity.ok(goalService.markCompleted(id));
    }

    @DeleteMapping("/{id:[0-9]+}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long id) {
        goalService.deleteGoal(id);
        return ResponseEntity.noContent().build();
    }

    // ── Milestones ─────────────────────────────────────────────────────

    @PostMapping("/{goalId}/milestones")
    public ResponseEntity<GoalMilestone> addMilestone(
            @PathVariable Long goalId,
            @RequestBody MilestoneRequestDTO req) {
        return new ResponseEntity<>(goalService.addMilestone(goalId, req), HttpStatus.CREATED);
    }

    @PutMapping("/{goalId}/milestones/{milestoneId}")
    public ResponseEntity<GoalMilestone> updateMilestone(
            @PathVariable Long goalId,
            @PathVariable Long milestoneId,
            @RequestBody MilestoneRequestDTO req) {
        return ResponseEntity.ok(goalService.updateMilestone(goalId, milestoneId, req));
    }

    @PatchMapping("/{goalId}/milestones/{milestoneId}/toggle")
    public ResponseEntity<GoalMilestone> toggleMilestone(
            @PathVariable Long goalId,
            @PathVariable Long milestoneId) {
        return ResponseEntity.ok(goalService.toggleMilestone(goalId, milestoneId));
    }

    @DeleteMapping("/{goalId}/milestones/{milestoneId}")
    public ResponseEntity<Void> deleteMilestone(
            @PathVariable Long goalId,
            @PathVariable Long milestoneId) {
        goalService.deleteMilestone(goalId, milestoneId);
        return ResponseEntity.noContent().build();
    }

    // ── Progress Journal Updates ───────────────────────────────────────

    @PostMapping("/{goalId}/updates")
    public ResponseEntity<GoalProgressUpdate> addProgressUpdate(
            @PathVariable Long goalId,
            @RequestBody ProgressUpdateRequestDTO req) {
        return new ResponseEntity<>(goalService.addProgressUpdate(goalId, req), HttpStatus.CREATED);
    }

    @DeleteMapping("/{goalId}/updates/{updateId}")
    public ResponseEntity<Void> deleteProgressUpdate(
            @PathVariable Long goalId,
            @PathVariable Long updateId) {
        goalService.deleteProgressUpdate(goalId, updateId);
        return ResponseEntity.noContent().build();
    }
}
