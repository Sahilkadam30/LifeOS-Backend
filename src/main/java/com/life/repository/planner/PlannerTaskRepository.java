package com.life.repository.planner;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.life.entity.planner.PlannerTask;

@Repository
public interface PlannerTaskRepository
        extends JpaRepository<PlannerTask, Long> {

    List<PlannerTask> findByUserId(Long userId);

    List<PlannerTask> findByUserIdAndStatus(
            Long userId,
            String status
    );

    List<PlannerTask> findByUserIdAndDueDateBetween(
            Long userId,
            LocalDate start,
            LocalDate end
    );

    long countByUserIdAndStatus(
            Long userId,
            String status
    );
}
