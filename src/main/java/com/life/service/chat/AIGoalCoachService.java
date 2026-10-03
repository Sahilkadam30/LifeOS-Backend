package com.life.service.chat;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.life.entity.goals.Goal;
import com.life.entity.goals.GoalMilestone;
import com.life.entity.goals.GoalProgressUpdate;
import com.life.entity.goals.GoalStatus;
import com.life.entity.gym.FitnessGoal;
import com.life.repository.goals.GoalMilestoneRepository;
import com.life.repository.goals.GoalRepository;
import com.life.repository.gym.FitnessGoalRepository;

@Service
public class AIGoalCoachService {

    private final GoalRepository goalRepository;
    private final GoalMilestoneRepository milestoneRepository;
    private final FitnessGoalRepository fitnessGoalRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    public AIGoalCoachService(
            GoalRepository goalRepository,
            GoalMilestoneRepository milestoneRepository,
            FitnessGoalRepository fitnessGoalRepository
    ) {
        this.goalRepository = goalRepository;
        this.milestoneRepository = milestoneRepository;
        this.fitnessGoalRepository = fitnessGoalRepository;
    }

    @Transactional(readOnly = true)
    public String buildGoalContext(Long userId) {

        List<Goal> goals = goalRepository.findByUserId(userId);
        List<FitnessGoal> fitnessGoals = fitnessGoalRepository != null ? fitnessGoalRepository.findByUserId(userId) : List.of();

        if (goals.isEmpty() && fitnessGoals.isEmpty()) {
            return "No life goals or fitness goals found yet.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== LIFE GOALS & ROADMAP PORTFOLIO ===\n\n");

        // 1. Overall Statistics & Metrics
        long totalGoals = goals.size();
        long completed = goals.stream().filter(g -> g.getStatus() == GoalStatus.COMPLETED).count();
        long inProgress = goals.stream().filter(g -> g.getStatus() == GoalStatus.IN_PROGRESS).count();
        long notStarted = goals.stream().filter(g -> g.getStatus() == GoalStatus.NOT_STARTED).count();
        double avgProgress = goals.isEmpty() ? 0.0 :
                goals.stream().mapToInt(Goal::getProgressPercentage).average().orElse(0.0);
        long completedMilestones = milestoneRepository.countCompletedMilestonesByUserId(userId);

        sb.append("Life Goals Overview & Statistics:\n");
        sb.append("- Total Goals: ").append(totalGoals).append("\n");
        sb.append("- Completed Goals: ").append(completed).append("\n");
        sb.append("- In-Progress Goals: ").append(inProgress).append("\n");
        sb.append("- Not Started Goals: ").append(notStarted).append("\n");
        sb.append("- Average Overall Progress: ").append(String.format("%.1f", avgProgress)).append("%\n");
        sb.append("- Completed Milestones: ").append(completedMilestones).append("\n\n");

        // 2. Detailed Life Goals
        if (!goals.isEmpty()) {
            sb.append("Life Goals Details:\n");
            for (Goal g : goals) {
                sb.append("• Goal: \"").append(g.getTitle()).append("\"\n");
                sb.append("  Horizon: ").append(g.getGoalType())
                  .append(" | Category: ").append(g.getCategory())
                  .append(" | Priority: ").append(g.getPriority()).append("\n");
                sb.append("  Status: ").append(g.getStatus())
                  .append(" | Progress: ").append(g.getProgressPercentage()).append("%\n");

                if (g.getDescription() != null && !g.getDescription().trim().isEmpty()) {
                    sb.append("  Description: ").append(g.getDescription().trim()).append("\n");
                }
                if (g.getStartDate() != null) {
                    sb.append("  Start Date: ").append(g.getStartDate().format(DATE_FORMATTER)).append("\n");
                }
                if (g.getTargetDate() != null) {
                    sb.append("  Target Deadline: ").append(g.getTargetDate().format(DATE_FORMATTER)).append("\n");
                }
                if (g.getCompletedAt() != null) {
                    sb.append("  Completed At: ").append(g.getCompletedAt().format(DATE_FORMATTER)).append("\n");
                }

                // Milestones
                if (g.getMilestones() != null && !g.getMilestones().isEmpty()) {
                    sb.append("  Milestones:\n");
                    for (GoalMilestone m : g.getMilestones()) {
                        sb.append("    - [").append(m.isCompleted() ? "X" : " ").append("] ")
                          .append(m.getTitle()).append("\n");
                    }
                }

                // Progress Update Notes
                if (g.getUpdates() != null && !g.getUpdates().isEmpty()) {
                    sb.append("  Recent Progress Notes:\n");
                    int noteCount = 0;
                    for (GoalProgressUpdate u : g.getUpdates()) {
                        if (noteCount++ >= 3) break;
                        sb.append("    * ").append(u.getNote()).append("\n");
                    }
                }
                sb.append("\n");
            }
        }

        // 3. Fitness Goals (from gym module if present)
        if (!fitnessGoals.isEmpty()) {
            sb.append("Fitness Goals Summary:\n");
            for (FitnessGoal fg : fitnessGoals) {
                sb.append("• Fitness Goal: ").append(fg.getGoalName())
                  .append(" | Status: ").append(fg.getStatus()).append("%\n");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
