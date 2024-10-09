package project.org.fitnessprogresstracker.service;

import project.org.fitnessprogresstracker.user.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    List<User> fetchUserList();
    User updateUser(User user, Long id);
    void deleteUserById(Long userId);


}
