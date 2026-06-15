package com.life.service.gym;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.life.entity.User;
import com.life.entity.gym.WeeklyMealPlan;
import com.life.repository.UserRepository;
import com.life.repository.gym.WeeklyMealPlanRepository;

@Service
public class MealPlanService {

	private final WeeklyMealPlanRepository mealRepository;
    private final UserRepository userRepository;

    public MealPlanService(
            WeeklyMealPlanRepository mealRepository,
            UserRepository userRepository) {

        this.mealRepository = mealRepository;
        this.userRepository = userRepository;
    }

    public WeeklyMealPlan create(
            WeeklyMealPlan meal) {

        User user = getCurrentUser();

        meal.setUserId(user.getId());

        return mealRepository.save(meal);
    }

    public List<WeeklyMealPlan> getAll() {

        User user = getCurrentUser();

        return mealRepository.findByUserId(
                user.getId()
        );
    }

    public WeeklyMealPlan getById(
            Long id) {

        User user = getCurrentUser();

        WeeklyMealPlan meal =
                mealRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Meal not found"));

        if (!meal.getUserId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        return meal;
    }

    public WeeklyMealPlan update(
            Long id,
            WeeklyMealPlan request) {

        User user = getCurrentUser();

        WeeklyMealPlan meal =
                mealRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Meal not found"));

        if (!meal.getUserId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        meal.setDayName(request.getDayName());
        meal.setMealType(request.getMealType());
        meal.setMealDescription(
                request.getMealDescription());

        return mealRepository.save(meal);
    }

    public void delete(
            Long id) {

        User user = getCurrentUser();

        WeeklyMealPlan meal =
                mealRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Meal not found"));

        if (!meal.getUserId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        mealRepository.delete(meal);
    }

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
