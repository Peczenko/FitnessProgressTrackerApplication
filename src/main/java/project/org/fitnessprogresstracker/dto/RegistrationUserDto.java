package project.org.fitnessprogresstracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RegistrationUserDto {
    private String username;
    private String email;
    private String password;
    private String passwordConfirm;
}
