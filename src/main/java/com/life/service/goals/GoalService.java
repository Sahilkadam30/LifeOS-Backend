package com.life.service.goals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.life.dto.goals.AchievementDTO;
import com.life.dto.goals.GoalRequestDTO;
import com.life.dto.goals.GoalStatsDTO;
import com.life.dto.goals.MilestoneRequestDTO;
import com.life.dto.goals.ProgressUpdateRequestDTO;
import com.life.entity.User;
import com.life.entity.goals.Goal;
import com.life.entity.goals.GoalCategory;
import com.life.entity.goals.GoalMilestone;
import com.life.entity.goals.GoalPriority;
import com.life.entity.goals.GoalProgressUpdate;
import com.life.entity.goals.GoalStatus;
import com.life.entity.goals.GoalType;
import com.life.repository.UserRepository;
import com.life.repository.goals.GoalMilestoneRepository;
import com.life.repository.goals.GoalProgressUpdateRepository;
import com.life.repository.goals.GoalRepository;

@Service
@Transactional
public class GoalService {

    private final GoalRepository goalRepository;
    private final GoalMilestoneRepository milestoneRepository;
    private final GoalProgressUpdateRepository updateRepository;
    private final UserRepository userRepository;

    public GoalService(
            GoalRepository goalRepository,
            GoalMilestoneRepository milestoneRepository,
            GoalProgressUpdateRepository updateRepository,
            UserRepository userRepository) {
        this.goalRepository = goalRepository;
        this.milestoneRepository = milestoneRepository;
        this.updateRepository = updateRepository;
        this.userRepository = userRepository;
    }

    public Goal createGoal(GoalRequestDTO req) {
        User user = getCurrentUser();

        validateGoalRequest(req);

        Goal goal = new Goal();
        goal.setUser(user);
        goal.setTitle(req.getTitle().trim());
        goal.setDescription(req.getDescription());
        goal.setGoalType(req.getGoalType() != null ? req.getGoalType() : GoalType.SHORT_TERM);
        goal.setCategory(req.getCategory() != null ? req.getCategory() : GoalCategory.PERSONAL);
        goal.setPriority(req.getPriority() != null ? req.getPriority() : GoalPriority.MEDIUM);

        int progress = req.getProgressPercentage() != null ? Math.max(0, Math.min(100, req.getProgressPercentage())) : 0;
        goal.setProgressPercentage(progress);

        GoalStatus status = req.getStatus() != null ? req.getStatus() : GoalStatus.NOT_STARTED;
        if (progress > 0 && status == GoalStatus.NOT_STARTED) {
            status = GoalStatus.IN_PROGRESS;
        }
        if (progress == 100) {
            status = GoalStatus.COMPLETED;
            goal.setCompletedAt(LocalDateTime.now());
        }
        goal.setStatus(status);

        goal.setStartDate(req.getStartDate());
        goal.setTargetDate(req.getTargetDate());

        return goalRepository.save(goal);
    }

    @Transactional(readOnly = true)
    public List<Goal> getAllGoals(GoalType type, GoalStatus status, GoalCategory category) {
        User user = getCurrentUser();
        List<Goal> goals = goalRepository.findByUserId(user.getId());

        return goals.stream()
                .filter(g -> type == null || g.getGoalType() == type)
                .filter(g -> status == null || g.getStatus() == status)
                .filter(g -> category == null || g.getCategory() == category)
                .toList();
    }

    @Transactional(readOnly = true)
    public Goal getGoalById(Long id) {
        User user = getCurrentUser();
        return goalRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Goal not found or access denied"));
    }

    public Goal updateGoal(Long id, GoalRequestDTO req) {
        User user = getCurrentUser();
        Goal goal = goalRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Goal not found or access denied"));

        validateGoalRequest(req);

        goal.setTitle(req.getTitle().trim());
        goal.setDescription(req.getDescription());
        if (req.getGoalType() != null) {
            goal.setGoalType(req.getGoalType());
        }
        if (req.getCategory() != null) {
            goal.setCategory(req.getCategory());
        }
        if (req.getPriority() != null) {
            goal.setPriority(req.getPriority());
        }
        goal.setStartDate(req.getStartDate());
        goal.setTargetDate(req.getTargetDate());

        if (req.getProgressPercentage() != null) {
            applyProgressUpdate(goal, req.getProgressPercentage());
        }

        if (req.getStatus() != null && req.getStatus() != goal.getStatus()) {
            GoalStatus oldStatus = goal.getStatus();
            GoalStatus newStatus = req.getStatus();
            goal.setStatus(newStatus);

            if (newStatus == GoalStatus.COMPLETED) {
                goal.setProgressPercentage(100);
                if (goal.getCompletedAt() == null) {
                    goal.setCompletedAt(LocalDateTime.now());
                }
            } else if (oldStatus == GoalStatus.COMPLETED) {
                goal.setCompletedAt(null);
                if (goal.getProgressPercentage() >= 100) {
                    goal.setProgressPercentage(99);
                }
            }
        }

        return goalRepository.save(goal);
    }

    public Goal updateProgress(Long id, Integer progressPercentage) {
        User user = getCurrentUser();
        Goal goal = goalRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Goal not found or access denied"));

        applyProgressUpdate(goal, progressPercentage);
        return goalRepository.save(goal);
    }

    public Goal markCompleted(Long id) {
        User user = getCurrentUser();
        Goal goal = goalRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Goal not found or access denied"));

        goal.setProgressPercentage(100);
        goal.setStatus(GoalStatus.COMPLETED);
        goal.setCompletedAt(LocalDateTime.now());

        return goalRepository.save(goal);
    }

    public void deleteGoal(Long id) {
        User user = getCurrentUser();
        Goal goal = goalRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Goal not found or access denied"));

        goalRepository.delete(goal);
    }

    // ── Milestone Operations ───────────────────────────────────────────

    public GoalMilestone addMilestone(Long goalId, MilestoneRequestDTO req) {
        User user = getCurrentUser();
        Goal goal = goalRepository.findByIdAndUserId(goalId, user.getId())
                .orElseThrow(() -> new RuntimeException("Goal not found or access denied"));

        if (req.getTitle() == null || req.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Milestone title is required");
        }

        GoalMilestone milestone = new GoalMilestone();
        milestone.setGoal(goal);
        milestone.setTitle(req.getTitle().trim());
        milestone.setCompleted(Boolean.TRUE.equals(req.getCompleted()));
        if (milestone.isCompleted()) {
            milestone.setCompletedAt(LocalDateTime.now());
        }

        int nextOrder = req.getOrderIndex() != null ? req.getOrderIndex() : goal.getMilestones().size();
        milestone.setOrderIndex(nextOrder);

        GoalMilestone saved = milestoneRepository.save(milestone);
        goal.getMilestones().add(saved);
        return saved;
    }

    public GoalMilestone updateMilestone(Long goalId, Long milestoneId, MilestoneRequestDTO req) {
        User user = getCurrentUser();
        GoalMilestone milestone = milestoneRepository.findByIdAndUserId(milestoneId, user.getId())
                .orElseThrow(() -> new RuntimeException("Milestone not found or access denied"));

        if (!milestone.getGoal().getId().equals(goalId)) {
            throw new RuntimeException("Milestone does not belong to specified goal");
        }

        if (req.getTitle() != null && !req.getTitle().trim().isEmpty()) {
            milestone.setTitle(req.getTitle().trim());
        }
        if (req.getOrderIndex() != null) {
            milestone.setOrderIndex(req.getOrderIndex());
        }
        if (req.getCompleted() != null) {
            boolean wasCompleted = milestone.isCompleted();
            milestone.setCompleted(req.getCompleted());
            if (!wasCompleted && req.getCompleted()) {
                milestone.setCompletedAt(LocalDateTime.now());
            } else if (wasCompleted && !req.getCompleted()) {
                milestone.setCompletedAt(null);
            }
        }

        return milestoneRepository.save(milestone);
    }

    public GoalMilestone toggleMilestone(Long goalId, Long milestoneId) {
        User user = getCurrentUser();
        GoalMilestone milestone = milestoneRepository.findByIdAndUserId(milestoneId, user.getId())
                .orElseThrow(() -> new RuntimeException("Milestone not found or access denied"));

        if (!milestone.getGoal().getId().equals(goalId)) {
            throw new RuntimeException("Milestone does not belong to specified goal");
        }

        boolean newState = !milestone.isCompleted();
        milestone.setCompleted(newState);
        milestone.setCompletedAt(newState ? LocalDateTime.now() : null);

        return milestoneRepository.save(milestone);
    }

    public void deleteMilestone(Long goalId, Long milestoneId) {
        User user = getCurrentUser();
        GoalMilestone milestone = milestoneRepository.findByIdAndUserId(milestoneId, user.getId())
                .orElseThrow(() -> new RuntimeException("Milestone not found or access denied"));

        if (!milestone.getGoal().getId().equals(goalId)) {
            throw new RuntimeException("Milestone does not belong to specified goal");
        }

        milestoneRepository.delete(milestone);
    }

    // ── Progress Journal / Updates Operations ─────────────────────────

    public GoalProgressUpdate addProgressUpdate(Long goalId, ProgressUpdateRequestDTO req) {
        User user = getCurrentUser();
        Goal goal = goalRepository.findByIdAndUserId(goalId, user.getId())
                .orElseThrow(() -> new RuntimeException("Goal not found or access denied"));

        if (req.getNote() == null || req.getNote().trim().isEmpty()) {
            throw new IllegalArgumentException("Progress update note cannot be empty");
        }

        GoalProgressUpdate update = new GoalProgressUpdate();
        update.setGoal(goal);
        update.setNote(req.getNote().trim());

        GoalProgressUpdate saved = updateRepository.save(update);
        goal.getUpdates().add(saved);
        return saved;
    }

    public void deleteProgressUpdate(Long goalId, Long updateId) {
        User user = getCurrentUser();
        GoalProgressUpdate update = updateRepository.findByIdAndUserId(updateId, user.getId())
                .orElseThrow(() -> new RuntimeException("Update note not found or access denied"));

        if (!update.getGoal().getId().equals(goalId)) {
            throw new RuntimeException("Update does not belong to specified goal");
        }

        updateRepository.delete(update);
    }

    // ── Stats and Achievements ─────────────────────────────────────────

    @Transactional(readOnly = true)
    public GoalStatsDTO getStats() {
        User user = getCurrentUser();
        Long userId = user.getId();

        long total = goalRepository.countByUserId(userId);
        long completed = goalRepository.countByUserIdAndStatus(userId, GoalStatus.COMPLETED);
        long inProgress = goalRepository.countByUserIdAndStatus(userId, GoalStatus.IN_PROGRESS);
        long notStarted = goalRepository.countByUserIdAndStatus(userId, GoalStatus.NOT_STARTED);
        long paused = goalRepository.countByUserIdAndStatus(userId, GoalStatus.PAUSED);
        long abandoned = goalRepository.countByUserIdAndStatus(userId, GoalStatus.ABANDONED);

        long shortTerm = goalRepository.countByUserIdAndGoalType(userId, GoalType.SHORT_TERM);
        long longTerm = goalRepository.countByUserIdAndGoalType(userId, GoalType.LONG_TERM);

        List<Goal> allGoals = goalRepository.findByUserId(userId);
        double avgProgress = allGoals.isEmpty() ? 0.0 :
                allGoals.stream().mapToInt(Goal::getProgressPercentage).average().orElse(0.0);

        GoalStatsDTO dto = new GoalStatsDTO();
        dto.setTotalGoals(total);
        dto.setCompleted(completed);
        dto.setInProgress(inProgress);
        dto.setNotStarted(notStarted);
        dto.setPaused(paused);
        dto.setAbandoned(abandoned);
        dto.setShortTermGoals(shortTerm);
        dto.setLongTermGoals(longTerm);
        dto.setOverallProgress(Math.round(avgProgress * 10.0) / 10.0);

        return dto;
    }

    @Transactional(readOnly = true)
    public List<AchievementDTO> getAchievements() {
        User user = getCurrentUser();
        Long userId = user.getId();

        long completedTotal = goalRepository.countByUserIdAndStatus(userId, GoalStatus.COMPLETED);
        long completedLongTerm = goalRepository.countByUserIdAndGoalTypeAndStatus(userId, GoalType.LONG_TERM, GoalStatus.COMPLETED);
        long completedMilestones = milestoneRepository.countCompletedMilestonesByUserId(userId);
        long totalGoals = goalRepository.countByUserId(userId);
        long updatesCount = updateRepository.countUpdatesByUserId(userId);

        List<AchievementDTO> achievements = new ArrayList<>();

        achievements.add(new AchievementDTO(
                "first_goal",
                "🏆",
                "First Goal",
                "Complete your first goal.",
                completedTotal >= 1,
                (int) Math.min(1, completedTotal),
                1,
                "Complete 1 goal"
        ));

        achievements.add(new AchievementDTO(
                "goal_getter",
                "🎯",
                "Goal Getter",
                "Complete 5 goals.",
                completedTotal >= 5,
                (int) Math.min(5, completedTotal),
                5,
                "Complete 5 goals"
        ));

        achievements.add(new AchievementDTO(
                "dream_chaser",
                "🌌",
                "Dream Chaser",
                "Complete your first long-term goal.",
                completedLongTerm >= 1,
                (int) Math.min(1, completedLongTerm),
                1,
                "Complete 1 long-term goal"
        ));

        achievements.add(new AchievementDTO(
                "consistent_achiever",
                "🌟",
                "Consistent Achiever",
                "Complete 10 goals across your life journey.",
                completedTotal >= 10,
                (int) Math.min(10, completedTotal),
                10,
                "Complete 10 goals"
        ));

        achievements.add(new AchievementDTO(
                "milestone_master",
                "🚩",
                "Milestone Master",
                "Complete 5 milestones along your path.",
                completedMilestones >= 5,
                (int) Math.min(5, completedMilestones),
                5,
                "Complete 5 milestones"
        ));

        achievements.add(new AchievementDTO(
                "journal_tracker",
                "📖",
                "Roadmap Chronicler",
                "Record 3 progress journal updates.",
                updatesCount >= 3,
                (int) Math.min(3, updatesCount),
                3,
                "Post 3 progress updates"
        ));

        return achievements;
    }

    // ── Helper Methods ─────────────────────────────────────────────────

    private void applyProgressUpdate(Goal goal, Integer newProgress) {
        if (newProgress == null) return;
        int clamped = Math.max(0, Math.min(100, newProgress));
        goal.setProgressPercentage(clamped);

        // Auto status transitions:
        // Do not automatically change PAUSED or ABANDONED unless explicitly changed
        if (goal.getStatus() != GoalStatus.PAUSED && goal.getStatus() != GoalStatus.ABANDONED) {
            if (clamped == 100) {
                goal.setStatus(GoalStatus.COMPLETED);
                if (goal.getCompletedAt() == null) {
                    goal.setCompletedAt(LocalDateTime.now());
                }
            } else if (clamped > 0) {
                goal.setStatus(GoalStatus.IN_PROGRESS);
                goal.setCompletedAt(null);
            } else if (clamped == 0 && goal.getStatus() == GoalStatus.COMPLETED) {
                goal.setStatus(GoalStatus.NOT_STARTED);
                goal.setCompletedAt(null);
            }
        }
    }

    private void validateGoalRequest(GoalRequestDTO req) {
        if (req.getTitle() == null || req.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Goal title is required");
        }
        if (req.getStartDate() != null && req.getTargetDate() != null) {
            if (req.getTargetDate().isBefore(req.getStartDate())) {
                throw new IllegalArgumentException("Target date cannot be earlier than start date");
            }
        }
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            throw new RuntimeException("Unauthorized: User not authenticated");
        }
        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found: " + auth.getName()));
    }
}
