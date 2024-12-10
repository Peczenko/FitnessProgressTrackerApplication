package project.org.fitnessprogresstracker.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Progress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date createdAt;
    private int weightStart;
    private int weightGoal;
    private int weightCurr;
    private int workoutsCompleted;
    private String notes;
    private int caloriesBurnt;
    private String goalType;
    private boolean isGoalAchieved;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

}
