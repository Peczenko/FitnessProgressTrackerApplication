package project.org.fitnessprogresstracker.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project.org.fitnessprogresstracker.dto.UserDto;
import project.org.fitnessprogresstracker.dto.WorkoutDto;
import project.org.fitnessprogresstracker.entities.User;
import project.org.fitnessprogresstracker.entities.Workout;
import project.org.fitnessprogresstracker.exceptions.AppError;
import project.org.fitnessprogresstracker.repository.WorkoutRepository;

import java.util.Collection;
import java.util.Date;
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
        workoutDTO.setUserDto(new UserDto(user.getUsername(), user.getEmail()));
        workoutDTO.setCreatedAt(new Date());
        Workout workout = Workout.builder()
                .user(user)
                .description(workoutDTO.getDescription())
                .duration(workoutDTO.getDuration())
                .name(workoutDTO.getName())
                .createdAt(new Date()).build();
        Workout savedWorkout = workoutRepository.save(workout);
        workout.setId(savedWorkout.getId());
        return workoutDTO;
    }

    public ResponseEntity<?> getWorkoutById(Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username).get();

        Optional<Workout> optionalWorkout = workoutRepository.findById(id);
        if (optionalWorkout.isEmpty()) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Workout with such id does not exist"), HttpStatus.BAD_REQUEST);
        }
        Workout workout = optionalWorkout.get();

        if (!workout.getUser().getUsername().equals(username)) {
            return new ResponseEntity<>(new AppError(HttpStatus.FORBIDDEN.value(), "Forbidden"), HttpStatus.FORBIDDEN);
        }
        WorkoutDto workoutDTO = WorkoutDto.builder()
                .id(workout.getId())
                .name(workout.getName())
                .createdAt(workout.getCreatedAt())
                .duration(workout.getDuration())
                .userDto(new UserDto(user.getUsername(), user.getEmail()))
                .description(workout.getDescription())
                .build();

        return ResponseEntity.ok(workoutDTO);
    }

    public ResponseEntity<List<WorkoutDto>> getAllWorkouts() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Collection<Workout> workouts = workoutRepository.findAllByUserUsername(username);
        List<WorkoutDto> convertedWorkouts = workouts.stream().map(this::convertToDto).collect(Collectors.toList());
        return ResponseEntity.ok(convertedWorkouts);
    }

    private WorkoutDto convertToDto(Workout workout) {
        UserDto userDto = UserDto.builder()
                .username(workout.getUser().getUsername())
                .email(workout.getUser().getEmail())
                .build();

        WorkoutDto workoutDTO = WorkoutDto.builder()
                .id(workout.getId())
                .name(workout.getName())
                .description(workout.getDescription())
                .createdAt(workout.getCreatedAt())
                .duration(workout.getDuration())
                .userDto(userDto)
                .build();
        return workoutDTO;
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
        if (workoutDto.getCreatedAt() != null) workout.setCreatedAt(new Date());

        workoutRepository.save(workout);
        return ResponseEntity.ok(convertToDto(workout));
    }
    public ResponseEntity<?> deleteWorkout(Long id){
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
        return ResponseEntity.ok(this.convertToDto(workout));

    }
}
