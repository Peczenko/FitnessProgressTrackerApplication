package project.org.fitnessprogresstracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserProfileDto {
    private Long id;
    private String username;
    private String email;
    private int age;
    private int weight;
    private int height;
}
