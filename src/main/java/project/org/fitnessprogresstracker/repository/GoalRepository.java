package project.org.fitnessprogresstracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.org.fitnessprogresstracker.entities.ExerciseType;
import project.org.fitnessprogresstracker.entities.Goal;

import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findAllByUserUsername(String username);

    List<Goal> findAllByExerciseTypeAndUserUsername(ExerciseType exerciseType, String username);

    List<Goal> findAllByCustomDescriptionAndUserUsername(String customDescription, String username);

    @Query("SELECT distinct g.customDescription FROM Goal g WHERE g.user.username = :username AND g.customDescription IS NOT NULL")
    List<String> findAllCustomDescriptionsByUsername(@Param("username") String username);

}
