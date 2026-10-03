package com.life.repository.goals;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.life.entity.goals.Goal;
import com.life.entity.goals.GoalStatus;
import com.life.entity.goals.GoalType;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {

    @Query("SELECT g FROM Goal g WHERE g.user.id = :userId ORDER BY g.createdAt DESC")
    List<Goal> findByUserId(@Param("userId") Long userId);

    @Query("SELECT g FROM Goal g WHERE g.id = :id AND g.user.id = :userId")
    Optional<Goal> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Query("SELECT g FROM Goal g WHERE g.user.id = :userId AND g.goalType = :goalType ORDER BY g.createdAt DESC")
    List<Goal> findByUserIdAndGoalType(@Param("userId") Long userId, @Param("goalType") GoalType goalType);

    @Query("SELECT g FROM Goal g WHERE g.user.id = :userId AND g.status = :status ORDER BY g.createdAt DESC")
    List<Goal> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") GoalStatus status);

    @Query("SELECT COUNT(g) FROM Goal g WHERE g.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(g) FROM Goal g WHERE g.user.id = :userId AND g.status = :status")
    long countByUserIdAndStatus(@Param("userId") Long userId, @Param("status") GoalStatus status);

    @Query("SELECT COUNT(g) FROM Goal g WHERE g.user.id = :userId AND g.goalType = :goalType")
    long countByUserIdAndGoalType(@Param("userId") Long userId, @Param("goalType") GoalType goalType);

    @Query("SELECT COUNT(g) FROM Goal g WHERE g.user.id = :userId AND g.goalType = :goalType AND g.status = :status")
    long countByUserIdAndGoalTypeAndStatus(@Param("userId") Long userId, @Param("goalType") GoalType goalType, @Param("status") GoalStatus status);
}
