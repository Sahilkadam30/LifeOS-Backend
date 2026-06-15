package com.life.controller.gym;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.life.entity.gym.GymWorkout;
import com.life.service.gym.GymWorkoutService;

@RestController
@RequestMapping("/api/gym/workouts")
public class GymWorkoutController {

	private final GymWorkoutService service;

    public GymWorkoutController(
            GymWorkoutService service) {
        this.service = service;
    }

    @PostMapping
    public GymWorkout create(
            @RequestBody GymWorkout workout){
        return service.create(workout);
    }

    @GetMapping
    public List<GymWorkout> getAll(){
        return service.getAll();
    }

    @PutMapping("/{id}")
    public GymWorkout update(
            @PathVariable Long id,
            @RequestBody GymWorkout workout){
        return service.update(id, workout);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id){
        service.delete(id);
    }

    
}