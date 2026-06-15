package com.life.dto;

import java.util.List;

import com.life.entity.gym.GymWorkout;
import com.life.entity.gym.WeeklyMealPlan;

public class DashboardResponse {

	private int workoutStreak;

    private long monthlyWorkoutCount;

    private long activeGoalsCount;

    private List<GymWorkout> recentWorkouts;

    private List<WeeklyMealPlan> mealPlans;

	public int getWorkoutStreak() {
		return workoutStreak;
	}

	public void setWorkoutStreak(int workoutStreak) {
		this.workoutStreak = workoutStreak;
	}

	public long getMonthlyWorkoutCount() {
		return monthlyWorkoutCount;
	}

	public void setMonthlyWorkoutCount(long monthlyWorkoutCount) {
		this.monthlyWorkoutCount = monthlyWorkoutCount;
	}

	public long getActiveGoalsCount() {
		return activeGoalsCount;
	}

	public void setActiveGoalsCount(long activeGoalsCount) {
		this.activeGoalsCount = activeGoalsCount;
	}

	public List<GymWorkout> getRecentWorkouts() {
		return recentWorkouts;
	}

	public void setRecentWorkouts(List<GymWorkout> recentWorkouts) {
		this.recentWorkouts = recentWorkouts;
	}

	public List<WeeklyMealPlan> getMealPlans() {
		return mealPlans;
	}

	public void setMealPlans(List<WeeklyMealPlan> mealPlans) {
		this.mealPlans = mealPlans;
	}
}
