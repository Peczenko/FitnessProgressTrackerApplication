package project.org.fitnessprogresstracker.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.org.fitnessprogresstracker.dto.JwtResponse;
import project.org.fitnessprogresstracker.dto.JwtRequest;
import project.org.fitnessprogresstracker.dto.RegistrationUserDto;
import project.org.fitnessprogresstracker.dto.UserDto;
import project.org.fitnessprogresstracker.exceptions.AppError;
import project.org.fitnessprogresstracker.repository.RoleRepository;
import project.org.fitnessprogresstracker.repository.UserRepository;
import project.org.fitnessprogresstracker.service.AuthService;
import project.org.fitnessprogresstracker.service.UserService;
import project.org.fitnessprogresstracker.user.Role;
import project.org.fitnessprogresstracker.user.User;
import project.org.fitnessprogresstracker.utils.JwtTokenUtils;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest){
        return authService.createAuthToken(authRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDto registrationUserDto){
        return authService.createNewUser(registrationUserDto);
    }

}
