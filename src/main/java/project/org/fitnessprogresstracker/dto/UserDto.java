package project.org.fitnessprogresstracker.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor
@Builder
public class UserDto {
    private String username;
    private String email;

}
