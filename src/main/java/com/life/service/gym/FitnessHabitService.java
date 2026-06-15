package com.life.service.gym;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.life.entity.User;
import com.life.entity.gym.FitnessHabit;
import com.life.repository.UserRepository;
import com.life.repository.gym.FitnessHabitRepository;

@Service
public class FitnessHabitService {

	private final FitnessHabitRepository repository;
    private final UserRepository userRepository;

    public FitnessHabitService(
            FitnessHabitRepository repository,
            UserRepository userRepository) {

        this.repository = repository;
        this.userRepository = userRepository;
    }

    public List<FitnessHabit> getTodayHabits() {

        User user = getCurrentUser();

        LocalDate today = LocalDate.now();

        List<FitnessHabit> habits =
                repository.findByUserIdAndHabitDate(
                        user.getId(),
                        today);

        if (habits.isEmpty()) {

            habits.add(createHabit(user, "Drink 3L Water"));
            habits.add(createHabit(user, "Gym"));
            habits.add(createHabit(user, "Protein Intake"));
            habits.add(createHabit(user, "Stretching"));
            habits.add(createHabit(user, "Sleep 8 Hours"));

            repository.saveAll(habits);
        }

        return habits;
    }

    public FitnessHabit toggle(Long id) {

        FitnessHabit habit =
                repository.findById(id)
                        .orElseThrow();

        habit.setCompleted(
                !habit.getCompleted());

        return repository.save(habit);
    }

    public FitnessHabit create(
            FitnessHabit habit) {

        User user = getCurrentUser();

        habit.setUserId(user.getId());

        if (habit.getHabitDate() == null) {
            habit.setHabitDate(LocalDate.now());
        }

        if (habit.getCompleted() == null) {
            habit.setCompleted(false);
        }

        return repository.save(habit);
    }
    
    private FitnessHabit createHabit(
            User user,
            String name) {

        FitnessHabit habit =
                new FitnessHabit();

        habit.setUserId(user.getId());
        habit.setHabitName(name);
        habit.setCompleted(false);
        habit.setHabitDate(LocalDate.now());

        return habit;
    }

    private User getCurrentUser() {

        String username =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        return userRepository
                .findByUsername(username)
                .orElseThrow();
    }
    
    public void delete(Long id) {

        FitnessHabit habit =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Habit not found"));

        repository.delete(habit);
    }
}
