package project.org.fitnessprogresstracker;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import project.org.fitnessprogresstracker.entities.Workout;
import project.org.fitnessprogresstracker.repository.WorkoutRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class DatabaseTest {
    @Autowired
    private WorkoutRepository workoutRepository;

    @Test
    public void testCreateWorkout() {
        Workout workout = Workout.builder()
                .name("Test Workout")
                .description("A workout for testing")
                .duration(60)
                .build();

        Workout savedWorkout = workoutRepository.save(workout);
        assertNotNull(savedWorkout.getId());
        assertEquals(savedWorkout.getId(), 1);
        assertEquals("Test Workout", savedWorkout.getName());
    }
}

