package project.org.fitnessprogresstracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.org.fitnessprogresstracker.entities.Workout;

import java.util.List;


@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    List<Workout> findAllByUserUsername(String user_username);

    @Query("SELECT DISTINCT e.customDescription FROM Workout w " +
            "JOIN w.exercises e " +
            "WHERE w.user.username = :username AND e.customDescription IS NOT NULL")
    List<String> findAllCustomDescriptionsByUsername(@Param("username") String username);
}
