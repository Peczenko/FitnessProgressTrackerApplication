package project.org.fitnessprogresstracker.dto;


import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class WorkoutDto {
    private String name;
    private String description;
    private int duration;
    private Date createdAt;
    private UserDto userDto;
}
