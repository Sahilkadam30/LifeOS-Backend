package com.life.dto;

import java.util.List;

import com.life.entity.planner.PlannerTask;

public class PlannerDashboardResponse {

	private Long totalTasks;

    private Long pendingTasks;

    private Long completedTasks;

    private Long overdueTasks;

    private List<PlannerTask> todayTasks;

    private List<PlannerTask> upcomingTasks;

	public Long getTotalTasks() {
		return totalTasks;
	}

	public void setTotalTasks(Long totalTasks) {
		this.totalTasks = totalTasks;
	}

	public Long getPendingTasks() {
		return pendingTasks;
	}

	public void setPendingTasks(Long pendingTasks) {
		this.pendingTasks = pendingTasks;
	}

	public Long getCompletedTasks() {
		return completedTasks;
	}

	public void setCompletedTasks(Long completedTasks) {
		this.completedTasks = completedTasks;
	}

	public Long getOverdueTasks() {
		return overdueTasks;
	}

	public void setOverdueTasks(Long overdueTasks) {
		this.overdueTasks = overdueTasks;
	}

	public List<PlannerTask> getTodayTasks() {
		return todayTasks;
	}

	public void setTodayTasks(List<PlannerTask> todayTasks) {
		this.todayTasks = todayTasks;
	}

	public List<PlannerTask> getUpcomingTasks() {
		return upcomingTasks;
	}

	public void setUpcomingTasks(List<PlannerTask> upcomingTasks) {
		this.upcomingTasks = upcomingTasks;
	}
}
