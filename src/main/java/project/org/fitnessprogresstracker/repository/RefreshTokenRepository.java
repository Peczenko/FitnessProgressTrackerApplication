package project.org.fitnessprogresstracker.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.org.fitnessprogresstracker.entities.RefreshToken;


import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUserUsername(String username);

    @Transactional
    void deleteByUserUsername(String username);
}
