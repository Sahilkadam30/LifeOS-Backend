package com.life.repository.goals;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.life.entity.goals.GoalMilestone;

@Repository
public interface GoalMilestoneRepository extends JpaRepository<GoalMilestone, Long> {

    @Query("SELECT m FROM GoalMilestone m WHERE m.goal.id = :goalId ORDER BY m.orderIndex ASC")
    List<GoalMilestone> findByGoalIdOrderByOrderIndexAsc(@Param("goalId") Long goalId);

    @Query("SELECT m FROM GoalMilestone m WHERE m.id = :id AND m.goal.user.id = :userId")
    Optional<GoalMilestone> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Query("SELECT COUNT(m) FROM GoalMilestone m WHERE m.goal.user.id = :userId AND m.completed = true")
    long countCompletedMilestonesByUserId(@Param("userId") Long userId);
}
