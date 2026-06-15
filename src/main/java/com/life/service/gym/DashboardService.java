package com.life.service.gym;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.life.entity.gym.GymWorkout;
import com.life.dto.DashboardResponse;
import com.life.entity.User;
import com.life.repository.UserRepository;
import com.life.repository.gym.FitnessGoalRepository;
import com.life.repository.gym.GymWorkoutRepository;
import com.life.repository.gym.WeeklyMealPlanRepository;

@Service
public class DashboardService {

    private final GymWorkoutRepository workoutRepo;
    private final FitnessGoalRepository goalRepo;
    private final WeeklyMealPlanRepository mealRepo;
    private final UserRepository userRepo;

    public DashboardService(
            GymWorkoutRepository workoutRepo,
            FitnessGoalRepository goalRepo,
            WeeklyMealPlanRepository mealRepo,
            UserRepository userRepo) {

        this.workoutRepo = workoutRepo;
        this.goalRepo = goalRepo;
        this.mealRepo = mealRepo;
        this.userRepo = userRepo;
    }

    public DashboardResponse getDashboard() {

        String username =
                SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user =
                userRepo.findByUsername(username)
                .orElseThrow();

        Long userId = user.getId();

        DashboardResponse response =
                new DashboardResponse();

        response.setMonthlyWorkoutCount(
                workoutRepo.countByUserIdAndWorkoutDateBetween(
                        userId,
                        LocalDate.now().withDayOfMonth(1),
                        LocalDate.now()
                )
        );

        response.setActiveGoalsCount(
                goalRepo.countByUserIdAndStatus(
                        userId,
                        "ACTIVE"
                )
        );

        response.setRecentWorkouts(
                workoutRepo
                        .findTop5ByUserIdOrderByWorkoutDateDesc(userId)
        );

        response.setMealPlans(
                mealRepo.findByUserId(userId)
        );

        response.setWorkoutStreak(
                calculateWorkoutStreak(userId)
        );

        return response;
    }

    private int calculateWorkoutStreak(Long userId){

        List<GymWorkout> workouts =
                workoutRepo.findByUserId(userId);

        Set<LocalDate> workoutDates =
                workouts.stream()
                .map(GymWorkout::getWorkoutDate)
                .collect(Collectors.toSet());

        int streak = 0;

        LocalDate current = LocalDate.now();

        while(workoutDates.contains(current)){
            streak++;
            current = current.minusDays(1);
        }

        return streak;
    }
}