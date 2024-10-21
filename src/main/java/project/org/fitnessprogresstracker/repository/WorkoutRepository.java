package project.org.fitnessprogresstracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.org.fitnessprogresstracker.entities.Workout;


@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    Boolean existsByUserId(Long user_id);

}
