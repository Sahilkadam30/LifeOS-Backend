package com.life.repository.gym;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.life.entity.gym.GymWorkout;

@Repository
public interface GymWorkoutRepository
        extends JpaRepository<GymWorkout, Long> {

    List<GymWorkout> findByUserId(Long userId);

    List<GymWorkout> findTop5ByUserIdOrderByWorkoutDateDesc(Long userId);

    long countByUserIdAndWorkoutDateBetween(
            Long userId,
            LocalDate start,
            LocalDate end
    );
 
    List<GymWorkout> findByUserIdOrderByWorkoutDateDesc(Long userId);
}