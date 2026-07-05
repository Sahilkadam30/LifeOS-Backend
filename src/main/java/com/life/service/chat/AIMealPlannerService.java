package com.life.service.chat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.life.entity.gym.WeeklyMealPlan;
import com.life.repository.gym.WeeklyMealPlanRepository;

@Service
public class AIMealPlannerService {

    @Autowired
    private WeeklyMealPlanRepository mealRepository;

    public String buildMealContext(Long userId) {

        List<WeeklyMealPlan> meals =
                mealRepository.findByUserId(userId);

        if(meals.isEmpty()) {
            return "No meal plan data available.";
        }

        StringBuilder context =
                new StringBuilder();

        context.append("Weekly Meal Plan:\n\n");

        for(WeeklyMealPlan meal : meals) {

            context.append(
                    "Day: "
                    + meal.getDayName()
                    + "\nMeal Type: "
                    + meal.getMealType()
                    + "\nFood: "
                    + meal.getMealDescription()
                    + "\n\n"
            );
        }

        return context.toString();
    }
}
