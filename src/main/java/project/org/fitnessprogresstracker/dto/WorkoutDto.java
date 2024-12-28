package project.org.fitnessprogresstracker.dto;


import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class WorkoutDto {
    private Long id;
    private String name;
    private String description;
    private int duration;
    private Date createdAt;
    private UserDto userDto;
    private List<ExerciseDto> exercises;
}
