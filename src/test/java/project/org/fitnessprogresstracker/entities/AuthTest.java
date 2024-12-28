package project.org.fitnessprogresstracker.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import project.org.fitnessprogresstracker.dto.JwtRequest;
import project.org.fitnessprogresstracker.dto.RegistrationUserDto;
import project.org.fitnessprogresstracker.service.AuthService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class AuthTest {
    private static final Logger log = LoggerFactory.getLogger(AuthTest.class);
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    AuthService authService;

    @Test
    public void testRegistration() throws Exception{
        RegistrationUserDto registrationUserDto = RegistrationUserDto.builder()
                .username("testUsername")
                .email("testGmail@gmail.com")
                .password("111111")
                .passwordConfirm("111111")
                .build();

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationUserDto)))
                .andExpect(status().is2xxSuccessful());

        Mockito.verify( authService,times(1)).createNewUser(Mockito.any(RegistrationUserDto.class));
        JwtRequest jwtRequest = JwtRequest.builder()
                .username(registrationUserDto.getUsername())
                .password(registrationUserDto.getPassword())
                .build();

        MvcResult result = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(jwtRequest)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        log.info("response: " + responseBody);
        assertThat(responseBody).startsWith("Bearer");

    }
}
