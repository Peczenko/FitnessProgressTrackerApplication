package project.org.fitnessprogresstracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.org.fitnessprogresstracker.entities.ExerciseType;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoalDto {
    private Long id;
    private String name;
    private ExerciseType exerciseType;
    private String customDescription;
    private Double targetWeight;
    private Integer targetReps;
    private Date deadline;
    private Date createdAt;
}
