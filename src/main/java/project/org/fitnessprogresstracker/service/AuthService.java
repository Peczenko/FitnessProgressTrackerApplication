package project.org.fitnessprogresstracker.service;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import project.org.fitnessprogresstracker.dto.*;
import project.org.fitnessprogresstracker.entities.*;
import project.org.fitnessprogresstracker.exceptions.AppError;
import project.org.fitnessprogresstracker.repository.RefreshTokenRepository;
import project.org.fitnessprogresstracker.repository.UserProfileInfoRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;
    private final UserProfileInfoRepository userProfileInfoRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenParser jwtTokenParser;

    public ResponseEntity<?> createAuthToken(JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "Incorrect username or password"), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String accessToken = jwtTokenService.generateAccessToken(userDetails);
        String refreshToken = jwtTokenService.generateRefreshToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(accessToken, refreshToken));
    }

    public ResponseEntity<?> createNewUser(RegistrationUserDto registrationUserDto) {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getPasswordConfirm())) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Passwords are not matching"), HttpStatus.BAD_REQUEST);
        }

        if (userService.findByUsername(registrationUserDto.getUsername()).isPresent()) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "User already exists"), HttpStatus.BAD_REQUEST);
        }

        User user = userService.createNewUser(registrationUserDto);
        return ResponseEntity.ok(new UserDto(user.getUsername(), user.getEmail()));
    }

    public ResponseEntity<?> getUserInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<User> optionalUser = userService.findByUsername(username);
        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "User does not exist"), HttpStatus.BAD_REQUEST);
        }

        Optional<UserProfile> userProfile = userProfileInfoRepository.findByUserUsername(username);
        if (userProfile.isEmpty()) {
            return ResponseEntity.ok(null);
        }
        UserProfileDto userProfileDto = this.userProfileToDto(userProfile.get(), optionalUser.get());

        return ResponseEntity.ok(userProfileDto);

    }

    public ResponseEntity<?> refreshAccessToken(TokenRefreshRequest tokenRefreshRequest) {
        String token = tokenRefreshRequest.getRefreshToken();

        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByToken(token);
        if (optionalRefreshToken.isEmpty()) {
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "Incorrect refresh token"), HttpStatus.UNAUTHORIZED);
        }

        if (!jwtTokenService.verifyExpiration(optionalRefreshToken.get())) {
            new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "Refresh token has expired. Please log in again."), HttpStatus.UNAUTHORIZED);
        }

        String username = jwtTokenParser.getUsername(token);
        UserDetails userDetails = userService.loadUserByUsername(username);
        String accessToken = jwtTokenService.generateAccessToken(userDetails);
        TokenRefreshResponse tokenRefreshResponse = new TokenRefreshResponse();
        tokenRefreshResponse.setAccessToken(accessToken);
        return ResponseEntity.ok(tokenRefreshResponse);

    }

    public ResponseEntity<?> logout() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByUserUsername(username);
        if (optionalRefreshToken.isEmpty()) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "You are already logged out"), HttpStatus.BAD_REQUEST);
        }
        refreshTokenRepository.deleteByUserUsername(username);
        return ResponseEntity.ok("You are logged out");

    }

    public ResponseEntity<?> setUserInfo(UserProfileDto userProfileDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<User> optionalUser = userService.findByUsername(username);
        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "User does not exist"), HttpStatus.BAD_REQUEST);
        }

        Optional<UserProfile> optionalUserProfile = userProfileInfoRepository.findByUserUsername(username);
        if (optionalUserProfile.isPresent()) {
            UserProfile userProfile = optionalUserProfile.get();

            userProfile.setAge(userProfileDto.getAge());
            userProfile.setHeight(userProfileDto.getHeight());
            userProfile.setWeight(userProfileDto.getWeight());
            userProfile.setUsername(userProfile.getUsername());
            userProfile.setEmail(userProfile.getEmail());

            userProfileInfoRepository.save(userProfile);
            return ResponseEntity.ok(Void.class);

        }

        User user = optionalUser.get();
        UserProfile userProfile = UserProfile.builder()
                .user(user)
                .age(userProfileDto.getAge())
                .height(userProfileDto.getHeight())
                .weight(userProfileDto.getWeight())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
        userProfileInfoRepository.save(userProfile);
        return ResponseEntity.ok(userProfileDto);
    }


    private UserProfileDto userProfileToDto(UserProfile userProfile, User user) {
        return UserProfileDto.builder()
                .age(userProfile.getAge())
                .height(userProfile.getHeight())
                .weight(userProfile.getWeight())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }


}
