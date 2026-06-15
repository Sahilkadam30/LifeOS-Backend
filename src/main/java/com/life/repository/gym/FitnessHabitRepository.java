package com.life.repository.gym;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.life.entity.gym.FitnessHabit;

@Repository
public interface FitnessHabitRepository extends JpaRepository<FitnessHabit, Long>{

	List<FitnessHabit> findByUserIdAndHabitDate(
            Long userId,
            LocalDate habitDate
    );

    long countByUserIdAndHabitDateAndCompleted(
            Long userId,
            LocalDate habitDate,
            Boolean completed
    );
}
