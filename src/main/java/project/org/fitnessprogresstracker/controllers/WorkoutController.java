package project.org.fitnessprogresstracker.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import project.org.fitnessprogresstracker.dto.WorkoutDTO;
import project.org.fitnessprogresstracker.entities.User;
import project.org.fitnessprogresstracker.entities.Workout;
import project.org.fitnessprogresstracker.repository.WorkoutRepository;
import project.org.fitnessprogresstracker.service.UserService;
import project.org.fitnessprogresstracker.service.WorkoutService;

import java.util.Date;

@RestController
@RequestMapping("/workouts")
@RequiredArgsConstructor
public class WorkoutController {
    private final WorkoutRepository workoutRepository;
    private final UserService userService;
    private final WorkoutService workoutService;


    @PostMapping("/add")
    public ResponseEntity<?> createWorkout(@RequestBody WorkoutDTO workoutDTO){
        WorkoutDTO savedWorkout = workoutService.createNewWorkout(workoutDTO);
        return ResponseEntity.ok(savedWorkout);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getWorkout(@PathVariable Long id){
       return workoutService.getWorkoutById(id);
    }


}
