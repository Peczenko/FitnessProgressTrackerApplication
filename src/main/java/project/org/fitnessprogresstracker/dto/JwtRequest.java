package project.org.fitnessprogresstracker.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtRequest {
    private String username;
    private String password;
}
