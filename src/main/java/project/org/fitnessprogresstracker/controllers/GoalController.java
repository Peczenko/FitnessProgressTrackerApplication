package project.org.fitnessprogresstracker.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.org.fitnessprogresstracker.dto.GoalDto;
import project.org.fitnessprogresstracker.entities.ExerciseType;
import project.org.fitnessprogresstracker.service.GoalService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/goals")
@AllArgsConstructor
public class GoalController {
    private GoalService goalService;


    @PostMapping("/add")
    public ResponseEntity<?> createGoal(@RequestBody GoalDto goalDto) {
        return goalService.createGoal(goalDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGoalById(@PathVariable Long id) {
        return goalService.getGoalById(id);
    }

    @GetMapping()
    public ResponseEntity<?> getAllGoals() {
        return goalService.getAllGoals();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGoal(@PathVariable Long id) {
        return goalService.deleteGoal(id);
    }

    @GetMapping("/exercise-type/{exerciseType}")
    public ResponseEntity<?> getGoalByExerciseType(@PathVariable ExerciseType exerciseType) {
        return goalService.getGoalByExerciseType(exerciseType);
    }

    @GetMapping("/custom-type/{customDescription}")
    public ResponseEntity<?> getGoalByCustomDescription(@PathVariable String customDescription) {
        return goalService.getGoalByCustomDescription(customDescription);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGoal(@PathVariable Long id, @RequestBody GoalDto goalDto) {
        return goalService.updateGoal(id, goalDto);
    }

    @GetMapping("/custom-type")
    public ResponseEntity<?> getAllCustomDescriptions() {
        return goalService.getAllCustomDescriptions();
    }

}
