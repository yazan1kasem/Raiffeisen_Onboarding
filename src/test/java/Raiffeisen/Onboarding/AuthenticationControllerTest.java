package Raiffeisen.Onboarding;

import Raiffeisen.Onboarding.Controller.AuthenticationController;
import Raiffeisen.Onboarding.Entities.User;
import Raiffeisen.Onboarding.JWT.dtos.LoginUserDto;
import Raiffeisen.Onboarding.JWT.dtos.RegisterUserDto;
import Raiffeisen.Onboarding.JWT.services.AuthenticationService;
import Raiffeisen.Onboarding.JWT.services.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private RegisterUserDto registerUserDto;
    private LoginUserDto loginUserDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("1");
        user.setUsername("testUser");
        user.setPassword("password123");
        user.setRole(User.Role.USER);

        registerUserDto = new RegisterUserDto();
        registerUserDto.setUsername("testUser");
        registerUserDto.setPassword("password");

        loginUserDto = new LoginUserDto();
        loginUserDto.setUsername("testUser");
        loginUserDto.setPassword("password");
    }

    @Test
    void testRegisterSuccess() throws Exception {
        Mockito.when(authenticationService.signup(any(RegisterUserDto.class))).thenReturn(user);

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.id").value("1")); // Ensure the ID is also returned
    }

    @Test
    void testRegisterBadRequest() throws Exception {
        registerUserDto.setPassword(""); // Invalid password

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerUserDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAuthenticateSuccess() throws Exception {
        String token = "mockedToken123";
        Mockito.when(authenticationService.authenticate(any(LoginUserDto.class))).thenReturn(user);
        Mockito.when(jwtService.generateToken(user)).thenReturn(token);
        Mockito.when(jwtService.getExpirationTime()).thenReturn(3600L);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token))
                .andExpect(jsonPath("$.expiresIn").value(3600))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.username").value("testUser")); // Ensure username is included
    }

    @Test
    void testAuthenticateUnauthorized() throws Exception {
        Mockito.when(authenticationService.authenticate(any(LoginUserDto.class)))
                .thenThrow(new IllegalArgumentException("Invalid credentials"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid credentials"));
    }

    @Test
    void testAuthenticateBadRequest() throws Exception {
        loginUserDto.setUsername(""); // Invalid username

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserDto)))
                .andExpect(status().isBadRequest());
    }
}
