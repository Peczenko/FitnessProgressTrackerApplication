package project.org.fitnessprogresstracker.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import project.org.fitnessprogresstracker.service.ProgressService;

import java.time.LocalDate;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/progress")
@RequiredArgsConstructor
public class ProgressController {
    private final ProgressService progressService;

    @GetMapping("/goal/{goalId}")
    public ResponseEntity<?> getGoalProgress(@PathVariable Long goalId) {
        return ResponseEntity.ok(progressService.calculateProgressForGoal(goalId));
    }

    @GetMapping("/common")
    public ResponseEntity<?> getCommonProgress() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(progressService.calculateCommonProgress(username));
    }

    @GetMapping("/goal/{goalId}/timeline")
    public ResponseEntity<?> getGoalProgressTimeline(@PathVariable Long goalId) {
        Map<LocalDate, Map<String, Object>> progressTimeline = progressService.calculateGoalProgressOverTime(goalId);
        return ResponseEntity.ok(progressTimeline);
    }

}
