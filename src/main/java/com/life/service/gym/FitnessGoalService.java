package com.life.service.gym;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.life.entity.User;
import com.life.entity.gym.FitnessGoal;
import com.life.repository.UserRepository;
import com.life.repository.gym.FitnessGoalRepository;

@Service
public class FitnessGoalService {

	private final FitnessGoalRepository goalRepository;
    private final UserRepository userRepository;

    public FitnessGoalService(
            FitnessGoalRepository goalRepository,
            UserRepository userRepository) {

        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
    }

    public FitnessGoal create(FitnessGoal goal) {

        User user = getCurrentUser();

        goal.setUserId(user.getId());

        if(goal.getCurrentValue() == null) {
            goal.setCurrentValue(0.0);
        }

        if(goal.getStatus() == null) {
            goal.setStatus("ACTIVE");
        }

        return goalRepository.save(goal);
    }

    public List<FitnessGoal> getAll() {

        User user = getCurrentUser();

        return goalRepository.findByUserId(user.getId());
    }

    public FitnessGoal getById(Long id) {

        User user = getCurrentUser();

        FitnessGoal goal =
                goalRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Goal not found"));

        if(!goal.getUserId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        return goal;
    }

    public FitnessGoal update(Long id, FitnessGoal request) {

        User user = getCurrentUser();

        FitnessGoal goal =
                goalRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Goal not found"));

        if(!goal.getUserId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        goal.setGoalName(request.getGoalName());
        goal.setTargetValue(request.getTargetValue());
        goal.setCurrentValue(request.getCurrentValue());
        goal.setTargetDate(request.getTargetDate());
        goal.setStatus(request.getStatus());

        return goalRepository.save(goal);
    }

    public FitnessGoal updateProgress(Long id, Double progress) {

        User user = getCurrentUser();

        FitnessGoal goal =
                goalRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Goal not found"));

        if(!goal.getUserId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        goal.setCurrentValue(progress);

        if(progress >= goal.getTargetValue()) {
            goal.setStatus("COMPLETED");
        }

        return goalRepository.save(goal);
    }

    public void delete(Long id) {

        User user = getCurrentUser();

        FitnessGoal goal =
                goalRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Goal not found"));

        if(!goal.getUserId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        goalRepository.delete(goal);
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
                        new RuntimeException("User not found"));
    }
}
