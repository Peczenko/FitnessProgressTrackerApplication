package project.org.fitnessprogresstracker.dto;

import lombok.Builder;
import lombok.Data;


import java.util.Date;

@Data
@Builder
public class ProgressDto {
    private Long id;
    private Date createdAt;
    private int weightStart;
    private int weightGoal;
    private int weightCurr;
    private int workoutsCompleted;
    private String notes;
    private int caloriesBurnt;
    private String goalType;
    private boolean isGoalAchieved;
    private UserDto userDto;

}
