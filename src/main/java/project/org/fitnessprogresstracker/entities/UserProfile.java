package project.org.fitnessprogresstracker.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfile {
    @Id
    private Long id;
    private String username;
    private String email;
    private int age;
    private int weight;
    private int height;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
}
