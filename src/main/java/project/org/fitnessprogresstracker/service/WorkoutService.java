package project.org.fitnessprogresstracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project.org.fitnessprogresstracker.dto.UserDto;
import project.org.fitnessprogresstracker.dto.WorkoutDTO;
import project.org.fitnessprogresstracker.entities.User;
import project.org.fitnessprogresstracker.entities.Workout;
import project.org.fitnessprogresstracker.exceptions.AppError;
import project.org.fitnessprogresstracker.repository.UserRepository;
import project.org.fitnessprogresstracker.repository.WorkoutRepository;

import java.util.Date;
import java.util.Optional;

@Service
public class WorkoutService {
    @Autowired
    private WorkoutRepository workoutRepository;
    @Autowired
    private UserService userService;

    public WorkoutDTO createNewWorkout(WorkoutDTO workoutDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username).get();
        workoutDTO.setUserDto(new UserDto(user.getUsername(), user.getEmail()));
        workoutDTO.setCreatedAt(new Date());
        return workoutDTO;
    }

    public ResponseEntity<?> getWorkoutById(Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username).get();

        Workout workout = workoutRepository.findById(id).get();
//        if (workout.not) {
//            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Workout with such id does not exist"), HttpStatus.BAD_REQUEST);
//        }

        if (!workout.getUser().getUsername().equals(username)) {
            return new ResponseEntity<>(new AppError(HttpStatus.FORBIDDEN.value(), "Forbidden"), HttpStatus.FORBIDDEN);
        }
        WorkoutDTO workoutDTO = WorkoutDTO.builder()
                .name(workout.getName())
                .createdAt(workout.getCreatedAt())
                .duration(workout.getDuration())
                .userDto(new UserDto(user.getUsername(), user.getEmail()))
                .description(workout.getDescription())
                .build();

        return ResponseEntity.ok(workoutDTO);
    }
}
