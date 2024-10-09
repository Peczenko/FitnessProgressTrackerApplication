package project.org.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginDTO {
    private String usernameOrEmail;
    private String password;
}
