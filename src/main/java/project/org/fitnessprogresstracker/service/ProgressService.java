package project.org.fitnessprogresstracker.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project.org.fitnessprogresstracker.dto.ProgressDto;
import project.org.fitnessprogresstracker.dto.UserDto;
import project.org.fitnessprogresstracker.entities.Progress;
import project.org.fitnessprogresstracker.entities.User;
import project.org.fitnessprogresstracker.exceptions.AppError;
import project.org.fitnessprogresstracker.repository.ProgressRepository;
import project.org.fitnessprogresstracker.repository.UserRepository;


import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProgressService {
    private final ProgressRepository progressRepository;
    private final UserRepository userRepository;

    public ResponseEntity<?> addProgress(ProgressDto progressDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> optionalUser = userRepository.findUserByUsername(username);
        if(optionalUser.isEmpty()){
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Something went wrong!"), HttpStatus.BAD_REQUEST);
        }
        User user = optionalUser.get();
        UserDto userDto = new UserDto(user.getUsername(), user.getEmail());
        Progress progress = Progress.builder()
                .createdAt(new Date())
                .weightStart(progressDto.getWeightStart())
                .weightGoal(progressDto.getWeightGoal())
                .weightCurr(progressDto.getWeightCurr())
                .workoutsCompleted(progressDto.getWorkoutsCompleted())
                .notes(progressDto.getNotes())
                .caloriesBurnt(progressDto.getCaloriesBurnt())
                .goalType(progressDto.getGoalType())
                .isGoalAchieved(progressDto.isGoalAchieved())
                .user(user)
                .build();
        progressDto.setUserDto(userDto);
        progressRepository.save(progress);

        return ResponseEntity.ok(progressDto);
    }

    public ResponseEntity<?> getProgresses() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!userRepository.existsUserByUsername(username)){
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "User doesn't exist"), HttpStatus.BAD_REQUEST);
        }

        List<Progress> progresses = progressRepository.findAllByUserUsername(username);
        List<ProgressDto> progressDtos = progresses.stream().map(this::convertToDto).toList();

        return ResponseEntity.ok(progressDtos);

    }

    public ResponseEntity<?> deleteProgress(Long id){
        Optional<Progress> progress = progressRepository.findById(id);
        if(progress.isEmpty()){
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Progress doesn't exist"), HttpStatus.BAD_REQUEST);
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!progress.get().getUser().getUsername().equals(username)){
            return new ResponseEntity<>(new AppError(HttpStatus.FORBIDDEN.value(), "Forbiddenn"), HttpStatus.FORBIDDEN);
        }
        progressRepository.deleteById(id);
        return ResponseEntity.ok(Void.class);
    }
    private ProgressDto convertToDto(Progress progress){
        UserDto userDto = UserDto.builder()
                .username(progress.getUser().getUsername())
                .email(progress.getUser().getEmail())
                .build();

        ProgressDto progressDto = ProgressDto.builder()
                .createdAt(progress.getCreatedAt())
                .weightStart(progress.getWeightStart())
                .weightGoal(progress.getWeightGoal())
                .weightCurr(progress.getWeightCurr())
                .workoutsCompleted(progress.getWorkoutsCompleted())
                .notes(progress.getNotes())
                .caloriesBurnt(progress.getCaloriesBurnt())
                .goalType(progress.getGoalType())
                .isGoalAchieved(progress.isGoalAchieved())
                .userDto(userDto)
                .build();
        return progressDto;

    }


}
