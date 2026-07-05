package com.life.service.chat;

import java.util.List;

import org.springframework.stereotype.Service;

import com.life.entity.gym.FitnessGoal;
import com.life.repository.gym.FitnessGoalRepository;

@Service
public class AIGoalCoachService {

    private final FitnessGoalRepository goalRepository;
    
    public AIGoalCoachService(FitnessGoalRepository goalRepository) {
    	this.goalRepository=goalRepository;
    }

    public String buildGoalContext(Long userId) {

        List<FitnessGoal> goals =
                goalRepository.findByUserId(userId);

        StringBuilder sb = new StringBuilder();

        sb.append("User Goals:\n");

        for(FitnessGoal goal : goals) {

            sb.append(
                    goal.getGoalName()
                    + " Progress="
                    + goal.getStatus()
                    + "%\n"
            );
        }

        return sb.toString();
    }
}
