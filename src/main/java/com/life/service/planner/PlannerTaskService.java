package com.life.service.planner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.life.entity.User;
import com.life.entity.planner.PlannerTask;
import com.life.repository.UserRepository;
import com.life.repository.planner.PlannerTaskRepository;

@Service
public class PlannerTaskService {

    private final PlannerTaskRepository taskRepository;
    private final UserRepository userRepository;

    public PlannerTaskService(
            PlannerTaskRepository taskRepository,
            UserRepository userRepository) {

        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public PlannerTask createTask(
            PlannerTask task) {

        User user = getCurrentUser();

        task.setUserId(user.getId());

        task.setCreatedAt(
                LocalDateTime.now()
        );

        if(task.getStatus() == null) {
            task.setStatus("PENDING");
        }

        if(task.getPriority() == null) {
            task.setPriority("MEDIUM");
        }

        return taskRepository.save(task);
    }

    public List<PlannerTask> getAllTasks() {

        User user = getCurrentUser();

        return taskRepository.findByUserId(
                user.getId()
        );
    }

    public PlannerTask getTaskById(
            Long id) {

        User user = getCurrentUser();

        PlannerTask task =
                taskRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Task not found"));

        if(!task.getUserId()
                .equals(user.getId())) {

            throw new RuntimeException(
                    "Access denied");
        }

        return task;
    }

    public PlannerTask updateTask(
            Long id,
            PlannerTask request) {

        User user = getCurrentUser();

        PlannerTask task =
                taskRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Task not found"));

        if(!task.getUserId()
                .equals(user.getId())) {

            throw new RuntimeException(
                    "Access denied");
        }

        task.setTitle(
                request.getTitle());

        task.setDescription(
                request.getDescription());

        task.setStartDate(
                request.getStartDate());

        task.setDueDate(
                request.getDueDate());

        task.setStartTime(
                request.getStartTime());

        task.setEndTime(
                request.getEndTime());

        task.setPriority(
                request.getPriority());

        task.setStatus(
                request.getStatus());

        task.setCategory(
                request.getCategory());

        task.setReminderEnabled(
                request.getReminderEnabled());

        return taskRepository.save(task);
    }

    public PlannerTask markCompleted(
            Long id) {

        User user = getCurrentUser();

        PlannerTask task =
                taskRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Task not found"));

        if(!task.getUserId()
                .equals(user.getId())) {

            throw new RuntimeException(
                    "Access denied");
        }

        task.setStatus("COMPLETED");

        return taskRepository.save(task);
    }

    public void deleteTask(
            Long id) {

        User user = getCurrentUser();

        PlannerTask task =
                taskRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Task not found"));

        if(!task.getUserId()
                .equals(user.getId())) {

            throw new RuntimeException(
                    "Access denied");
        }

        taskRepository.delete(task);
    }

    public List<PlannerTask> getUpcomingTasks() {

        User user = getCurrentUser();

        LocalDate today =
                LocalDate.now();

        LocalDate nextWeek =
                today.plusDays(7);

        return taskRepository
                .findByUserIdAndDueDateBetween(
                        user.getId(),
                        today,
                        nextWeek
                );
    }

    private User getCurrentUser() {

        String username =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        return userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"));
    }
}
