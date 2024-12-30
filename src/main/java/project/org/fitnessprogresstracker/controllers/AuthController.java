package project.org.fitnessprogresstracker.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.org.fitnessprogresstracker.dto.JwtRequest;
import project.org.fitnessprogresstracker.dto.RegistrationUserDto;
import project.org.fitnessprogresstracker.dto.UserProfileDto;
import project.org.fitnessprogresstracker.entities.TokenRefreshRequest;
import project.org.fitnessprogresstracker.service.AuthService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        return authService.createAuthToken(authRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDto registrationUserDto) {
        return authService.createNewUser(registrationUserDto);
    }

    @GetMapping("/me")
    public ResponseEntity<?> userInfo() {
        return authService.getUserInfo();
    }

    @PostMapping("/me")
    public ResponseEntity<?> setUserInfo(@RequestBody UserProfileDto userProfileDto) {
        return authService.setUserInfo(userProfileDto);
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest tokenRefreshRequest) {
        return authService.refreshAccessToken(tokenRefreshRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return authService.logout();
    }

}
