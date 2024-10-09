package project.org.fitnessprogresstracker.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.org.fitnessprogresstracker.repository.UserRepository;
import project.org.fitnessprogresstracker.user.User;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    UserRepository userRepository;
    @GetMapping
    public ResponseEntity<List<User>> getUsers(){
       return ResponseEntity.ok(userRepository.findAll());
    }

    @PostMapping("/save")
    public ResponseEntity<User> saveUser(@RequestBody User user){
        return ResponseEntity.ok(userRepository.save(user));

    }
    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody User user){
        return ResponseEntity.ok(Void.class);

    }



}
