package project.org.fitnessprogresstracker.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project.org.fitnessprogresstracker.dto.WorkoutDto;
import project.org.fitnessprogresstracker.entities.Exercise;
import project.org.fitnessprogresstracker.entities.User;
import project.org.fitnessprogresstracker.entities.Workout;
import project.org.fitnessprogresstracker.exceptions.AppError;
import project.org.fitnessprogresstracker.repository.WorkoutRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final UserService userService;

    public WorkoutDto createNewWorkout(WorkoutDto workoutDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username).get();

        Workout workout = WorkoutMapper.convertToEntity(workoutDTO, user);
        Workout savedWorkout = workoutRepository.save(workout);

        return WorkoutMapper.convertWorkoutToDto(savedWorkout);
    }

    public ResponseEntity<?> getWorkoutById(Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();


        Optional<Workout> optionalWorkout = workoutRepository.findById(id);
        if (optionalWorkout.isEmpty()) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Workout with such id does not exist"), HttpStatus.BAD_REQUEST);
        }
        Workout workout = optionalWorkout.get();

        if (!workout.getUser().getUsername().equals(username)) {
            return new ResponseEntity<>(new AppError(HttpStatus.FORBIDDEN.value(), "Forbidden"), HttpStatus.FORBIDDEN);
        }

        WorkoutDto workoutDto = WorkoutMapper.convertWorkoutToDto(workout);
        return ResponseEntity.ok(workoutDto);
    }

    public ResponseEntity<List<WorkoutDto>> getAllWorkouts() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Collection<Workout> workouts = workoutRepository.findAllByUserUsername(username);
        List<WorkoutDto> convertedWorkouts = workouts.stream().map(WorkoutMapper::convertWorkoutToDto).collect(Collectors.toList());
        return ResponseEntity.ok(convertedWorkouts);
    }

    public ResponseEntity<?> updateWorkout(Long id, WorkoutDto workoutDto) {
        Optional<Workout> optionalWorkout = workoutRepository.findById(id);

        if (optionalWorkout.isEmpty()) {
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(), "Workout not found"), HttpStatus.NOT_FOUND);
        }

        Workout workout = optionalWorkout.get();
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!workout.getUser().getUsername().equals(currentUsername)) {
            return new ResponseEntity<>(new AppError(HttpStatus.FORBIDDEN.value(), "You do not have permission to update this workout"), HttpStatus.FORBIDDEN);
        }


        if (workoutDto.getName() != null) workout.setName(workoutDto.getName());
        if (workoutDto.getDescription() != null) workout.setDescription(workoutDto.getDescription());
        if (workoutDto.getDuration() > 0) workout.setDuration(workoutDto.getDuration());
        if (workoutDto.getCreatedAt() != null) workout.setCreatedAt(workoutDto.getCreatedAt());


        List<Exercise> updatedExercises = workoutDto.getExercises().stream()
                .map(exerciseDto -> WorkoutMapper.updateExerciseFromDto(exerciseDto, workout, workout.getExercises()))
                .collect(Collectors.toList());

        workout.getExercises().removeIf(existingExercise ->
                updatedExercises.stream().noneMatch(updatedExercise -> updatedExercise.getId() != null &&
                        updatedExercise.getId().equals(existingExercise.getId())));

        workout.getExercises().clear();
        workout.getExercises().addAll(updatedExercises);


        workoutRepository.save(workout);
        return ResponseEntity.ok(WorkoutMapper.convertWorkoutToDto(workout));
    }

    public ResponseEntity<?> deleteWorkout(Long id) {
        Optional<Workout> optionalWorkout = workoutRepository.findById(id);

        if (optionalWorkout.isEmpty()) {
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(), "Workout not found"), HttpStatus.NOT_FOUND);
        }

        Workout workout = optionalWorkout.get();
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!workout.getUser().getUsername().equals(currentUsername)) {
            return new ResponseEntity<>(new AppError(HttpStatus.FORBIDDEN.value(), "You do not have permission to update this workout"), HttpStatus.FORBIDDEN);
        }

        workoutRepository.deleteById(id);
        return ResponseEntity.ok(WorkoutMapper.convertWorkoutToDto(workout));

    }
    public ResponseEntity<?> getAllExerciseCustomTypes(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<String> exerciseList = workoutRepository.findAllCustomDescriptionsByUsername(username);
        return ResponseEntity.ok(exerciseList);

    }
}
