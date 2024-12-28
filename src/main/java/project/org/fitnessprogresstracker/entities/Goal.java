package project.org.fitnessprogresstracker.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Data
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ExerciseType exerciseType;
    private String customDescription;


    private Double targetWeight;
    private Integer targetReps;

    private Date createdAt;
    private Date deadline;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
