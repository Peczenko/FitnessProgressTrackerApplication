package project.org.fitnessprogresstracker.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Data
@Table(name = "exercise_set")
public class Set {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int reps; // Number of repetitions in this set
    private double weight; // Weight lifted in this set (kg)

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;
}

