package com.life.repository.gym;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.life.entity.gym.FitnessGoal;

@Repository
public interface FitnessGoalRepository
        extends JpaRepository<FitnessGoal, Long> {

    List<FitnessGoal> findByUserId(Long userId);

    long countByUserIdAndStatus(
            Long userId,
            String status
    );
}