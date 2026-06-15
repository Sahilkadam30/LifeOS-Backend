package com.life.service.gym;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.life.entity.User;
import com.life.entity.gym.GymWorkout;
import com.life.repository.UserRepository;
import com.life.repository.gym.GymWorkoutRepository;

@Service
public class GymWorkoutService {

	private final GymWorkoutRepository workoutRepository;
    private final UserRepository userRepository;

    public GymWorkoutService(
            GymWorkoutRepository workoutRepository,
            UserRepository userRepository) {

        this.workoutRepository = workoutRepository;
        this.userRepository = userRepository;
    }

    /**
     * Create Workout
     */
    public GymWorkout create(GymWorkout workout) {

        User user = getCurrentUser();

        workout.setUserId(user.getId());

        return workoutRepository.save(workout);
    }

    /**
     * Get All Workouts of Logged In User
     */
    public List<GymWorkout> getAll() {

        User user = getCurrentUser();

        return workoutRepository.findByUserIdOrderByWorkoutDateDesc(
                user.getId()
        );
    }

    /**
     * Get Workout By Id
     */
    public GymWorkout getById(Long id) {

        User user = getCurrentUser();

        GymWorkout workout =
                workoutRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Workout not found"));

        if (!workout.getUserId().equals(user.getId())) {
            throw new RuntimeException("Access Denied");
        }

        return workout;
    }

    /**
     * Update Workout
     */
    public GymWorkout update(Long id, GymWorkout request) {

        User user = getCurrentUser();

        GymWorkout workout =
                workoutRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Workout not found"));

        if (!workout.getUserId().equals(user.getId())) {
            throw new RuntimeException("Access Denied");
        }

        workout.setWorkoutName(request.getWorkoutName());
        workout.setWorkoutType(request.getWorkoutType());
        workout.setMuscleGroup(request.getMuscleGroup());
        workout.setDurationMinutes(request.getDurationMinutes());
        workout.setWorkoutDate(request.getWorkoutDate());
        workout.setNotes(request.getNotes());

        return workoutRepository.save(workout);
    }

    /**
     * Delete Workout
     */
    public void delete(Long id) {

        User user = getCurrentUser();

        GymWorkout workout =
                workoutRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Workout not found"));

        if (!workout.getUserId().equals(user.getId())) {
            throw new RuntimeException("Access Denied");
        }

        workoutRepository.delete(workout);
    }

    /**
     * Current Logged In User
     */
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

