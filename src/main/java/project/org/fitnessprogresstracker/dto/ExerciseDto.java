package project.org.fitnessprogresstracker.dto;

import lombok.Builder;
import lombok.Data;
import project.org.fitnessprogresstracker.entities.ExerciseType;

import java.util.List;

@Builder
@Data
public class ExerciseDto {
    private Long id;
    private ExerciseType exerciseType;
    private String customDescription;
    private List<SetDto> sets;
}
