package project.org.fitnessprogresstracker.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SetDto {
    private Long id;
    private int reps;
    private double weight;
}
