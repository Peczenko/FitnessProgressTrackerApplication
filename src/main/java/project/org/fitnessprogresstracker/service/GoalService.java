package project.org.fitnessprogresstracker.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project.org.fitnessprogresstracker.dto.GoalDto;
import project.org.fitnessprogresstracker.entities.ExerciseType;
import project.org.fitnessprogresstracker.entities.Goal;
import project.org.fitnessprogresstracker.entities.User;
import project.org.fitnessprogresstracker.exceptions.AppError;
import project.org.fitnessprogresstracker.repository.GoalRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class GoalService {
    private static final Logger log = LoggerFactory.getLogger(GoalService.class);
    private final GoalRepository goalRepository;
    private final UserService userService;

    public ResponseEntity<?> createGoal(GoalDto goalDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(), "User with such username is not found"), HttpStatus.NOT_FOUND);
        }
        User user = userOptional.get();

        Goal goal = Goal.builder()
                .name(goalDto.getName())
                .exerciseType(goalDto.getExerciseType())
                .customDescription(goalDto.getCustomDescription())
                .targetWeight(goalDto.getTargetWeight())
                .targetReps(goalDto.getTargetReps())
                .deadline(goalDto.getDeadline())
                .createdAt(new Date())
                .user(user)
                .build();

        Goal savedGoal = goalRepository.save(goal);
        return ResponseEntity.ok(convertToDto(savedGoal));
    }

    public ResponseEntity<?> getGoalById(Long id) {
        Optional<Goal> optionalGoal = goalRepository.findById(id);
        if (optionalGoal.isEmpty()) {
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(), "Goal is not found"), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(convertToDto(optionalGoal.get()));
    }

    public ResponseEntity<?> getAllGoals() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Goal> goals = goalRepository.findAllByUserUsername(username);
        List<GoalDto> goalsDto = goals.stream().map(this::convertToDto).toList();
        return ResponseEntity.ok(goalsDto);
    }

    public ResponseEntity<?> updateGoal(Long id, GoalDto updatedGoalDto) {
        Optional<Goal> optionalGoal = goalRepository.findById(id);
        if (optionalGoal.isEmpty()) {
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(), "Goal is not found"), HttpStatus.NOT_FOUND);
        }

        Goal goal = optionalGoal.get();

        if (updatedGoalDto.getName() != null) goal.setName(updatedGoalDto.getName());

        if (updatedGoalDto.getExerciseType() != null) {
            goal.setExerciseType(updatedGoalDto.getExerciseType());
            goal.setCustomDescription(null); // Clear custom description when exerciseType is set
        }

        if (updatedGoalDto.getCustomDescription() != null) {
            goal.setCustomDescription(updatedGoalDto.getCustomDescription());
            goal.setExerciseType(null); // Clear exerciseType when customDescription is set
        }

        if (updatedGoalDto.getTargetWeight() != null) goal.setTargetWeight(updatedGoalDto.getTargetWeight());
        if (updatedGoalDto.getTargetReps() != null) goal.setTargetReps(updatedGoalDto.getTargetReps());
        if (updatedGoalDto.getDeadline() != null) goal.setDeadline(updatedGoalDto.getDeadline());
        if (updatedGoalDto.getCreatedAt() != null) goal.setCreatedAt(updatedGoalDto.getCreatedAt());

        Goal updatedGoal = goalRepository.save(goal);
        return ResponseEntity.ok(convertToDto(updatedGoal));
    }

    public ResponseEntity<?> deleteGoal(Long id) {
        Optional<Goal> optionalGoal = goalRepository.findById(id);
        if (optionalGoal.isEmpty()) {
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(), "Goal is not found"), HttpStatus.NOT_FOUND);
        }
        Goal goal = optionalGoal.get();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!goal.getUser().getUsername().equals(username)) {
            return new ResponseEntity<>(new AppError(HttpStatus.FORBIDDEN.value(), "You do not have permission to update this goal"), HttpStatus.FORBIDDEN);
        }

        goalRepository.deleteById(id);
        return ResponseEntity.ok(convertToDto(goal));
    }

    public ResponseEntity<?> getGoalByExerciseType(ExerciseType exerciseType) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Goal> goals = goalRepository.findAllByExerciseTypeAndUserUsername(exerciseType, username);

        List<GoalDto> goalDtos = goals.stream().map(this::convertToDto).toList();
        return ResponseEntity.ok(goalDtos);
    }

    public ResponseEntity<?> getGoalByCustomDescription(String customDescription) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Goal> goals = goalRepository.findAllByCustomDescriptionAndUserUsername(customDescription, username);

        List<GoalDto> goalDtos = goals.stream().map(this::convertToDto).toList();

        return ResponseEntity.ok(goalDtos);

    }

    public ResponseEntity<?> getAllCustomDescriptions() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<String> customDescriptions = goalRepository.findAllCustomDescriptionsByUsername(username);
        return ResponseEntity.ok(customDescriptions);
    }

    private GoalDto convertToDto(Goal goal) {
        return GoalDto.builder()
                .id(goal.getId())
                .name(goal.getName())
                .exerciseType(goal.getExerciseType())
                .customDescription(goal.getCustomDescription())
                .targetWeight(goal.getTargetWeight())
                .targetReps(goal.getTargetReps())
                .deadline(goal.getDeadline())
                .createdAt(goal.getCreatedAt())
                .build();
    }
}

