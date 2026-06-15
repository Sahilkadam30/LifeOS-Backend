package com.life.controller.gym;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.life.entity.gym.FitnessHabit;
import com.life.service.gym.FitnessHabitService;

@RestController
@RequestMapping("/api/gym/habits")
public class FitnessHabitController {

	private final FitnessHabitService service;

    public FitnessHabitController(
            FitnessHabitService service) {

        this.service = service;
    }

    @GetMapping
    public List<FitnessHabit> getTodayHabits() {

        return service.getTodayHabits();
    }

    @PostMapping
    public FitnessHabit create(
            @RequestBody FitnessHabit habit) {

        return service.create(habit);
    }
    
    @PutMapping("/{id}")
    public FitnessHabit toggle(
            @PathVariable Long id) {
    	   System.out.println("Toggle Habit: " + id);

        return service.toggle(id);
    }
    
    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id) {

        service.delete(id);
    }
}
