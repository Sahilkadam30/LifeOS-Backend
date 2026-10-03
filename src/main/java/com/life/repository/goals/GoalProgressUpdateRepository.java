package com.life.repository.goals;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.life.entity.goals.GoalProgressUpdate;

@Repository
public interface GoalProgressUpdateRepository extends JpaRepository<GoalProgressUpdate, Long> {

    @Query("SELECT u FROM GoalProgressUpdate u WHERE u.goal.id = :goalId ORDER BY u.createdAt DESC")
    List<GoalProgressUpdate> findByGoalIdOrderByCreatedAtDesc(@Param("goalId") Long goalId);

    @Query("SELECT u FROM GoalProgressUpdate u WHERE u.id = :id AND u.goal.user.id = :userId")
    Optional<GoalProgressUpdate> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Query("SELECT COUNT(u) FROM GoalProgressUpdate u WHERE u.goal.user.id = :userId")
    long countUpdatesByUserId(@Param("userId") Long userId);
}
