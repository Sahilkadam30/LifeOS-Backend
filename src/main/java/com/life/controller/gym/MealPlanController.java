package com.life.controller.gym;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.life.entity.gym.WeeklyMealPlan;
import com.life.service.gym.MealPlanService;

@RestController
@RequestMapping("/api/gym/meals")
public class MealPlanController {

	private final MealPlanService service;

    public MealPlanController(
            MealPlanService service) {
        this.service = service;
    }

    @PostMapping
    public WeeklyMealPlan create(
            @RequestBody WeeklyMealPlan meal) {

        return service.create(meal);
    }

    @GetMapping
    public List<WeeklyMealPlan> getAll() {

        return service.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id) {

        service.delete(id);
    }
    
    @GetMapping("/{id}")
    public WeeklyMealPlan getById(
            @PathVariable Long id) {

        return service.getById(id);
    }

    @PutMapping("/{id}")
    public WeeklyMealPlan update(
            @PathVariable Long id,
            @RequestBody WeeklyMealPlan meal) {

        return service.update(id, meal);
    }
}
