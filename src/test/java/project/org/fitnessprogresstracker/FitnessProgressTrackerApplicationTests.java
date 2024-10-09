package project.org.fitnessprogresstracker;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import project.org.dto.LoginDTO;
import project.org.dto.SignUpDTO;
import project.org.fitnessprogresstracker.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FitnessProgressTrackerApplicationTests {
    @Autowired
    TestRestTemplate testRestTemplate;
    @Autowired
    UserRepository userRepository;
    @Test
    void signNnTest(){
        SignUpDTO signUpDTO = SignUpDTO.builder().
                name("testName").
                email("test@gmail.com").
                username("testUsername").
                password("password").build();
        testRestTemplate.postForEntity("/api/auth/signup", signUpDTO, Void.class );
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        LoginDTO loginDTO = LoginDTO.builder().
                usernameOrEmail("testName").
                password("password").
                build();
        ResponseEntity response = testRestTemplate.postForEntity("/api/auth/signin", loginDTO, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @AfterEach
     void cleanDB(){
        userRepository.deleteAll();
    }




}
