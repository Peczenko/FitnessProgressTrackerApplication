package project.org.fitnessprogresstracker.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.org.fitnessprogresstracker.repository.RoleRepository;
import project.org.fitnessprogresstracker.user.Role;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role getUserRole() {
        return roleRepository.findByName("ROLE_USER").get();
    }
}
