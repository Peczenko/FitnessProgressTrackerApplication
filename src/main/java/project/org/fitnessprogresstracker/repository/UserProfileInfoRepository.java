package project.org.fitnessprogresstracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.org.fitnessprogresstracker.entities.UserProfile;

import java.util.Optional;

public interface UserProfileInfoRepository extends JpaRepository<UserProfile,Long> {
    Optional<UserProfile> findByUserUsername(String username);
}
