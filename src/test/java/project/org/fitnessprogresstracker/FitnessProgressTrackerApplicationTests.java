package project.org.fitnessprogresstracker;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import project.org.fitnessprogresstracker.controllers.AuthController;
import project.org.fitnessprogresstracker.dto.RegistrationUserDto;
import project.org.fitnessprogresstracker.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FitnessProgressTrackerApplicationTests {
    @Autowired
    TestRestTemplate testRestTemplate;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthController authController;
    @Test
    void signNnTest(){
        RegistrationUserDto registrationUserDto = RegistrationUserDto.builder().
                username("testName").
                email("test@gmail.com").
                password("password").
                confirmPassword("password").
                build();
        ResponseEntity<Void> response =  testRestTemplate.postForEntity("/api/auth/register", registrationUserDto, Void.class );
        System.out.println(response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }




}
