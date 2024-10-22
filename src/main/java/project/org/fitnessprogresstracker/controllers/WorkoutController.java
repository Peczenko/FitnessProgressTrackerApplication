package project.org.fitnessprogresstracker.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.org.fitnessprogresstracker.dto.WorkoutDto;
import project.org.fitnessprogresstracker.service.WorkoutService;


@RestController
@RequestMapping("/workouts")
@RequiredArgsConstructor
public class WorkoutController {
    private final WorkoutService workoutService;


    @PostMapping("/add")
    public ResponseEntity<?> createWorkout(@RequestBody WorkoutDto workoutDTO) {
        WorkoutDto savedWorkout = workoutService.createNewWorkout(workoutDTO);
        return ResponseEntity.ok(savedWorkout);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getWorkout(@PathVariable Long id) {
        return workoutService.getWorkoutById(id);
    }

    @GetMapping()
    public ResponseEntity<?> getAllWorkouts() {
        return workoutService.getAllWorkouts();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateWorkout(@PathVariable Long id, @RequestBody WorkoutDto workoutDto) {
        return workoutService.updateWorkout(id, workoutDto);
    }


}
