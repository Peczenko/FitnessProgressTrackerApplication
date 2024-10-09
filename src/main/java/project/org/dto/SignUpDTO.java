package project.org.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUpDTO {
    private String name;
    private String username;
    private String email;
    private String password;
}
