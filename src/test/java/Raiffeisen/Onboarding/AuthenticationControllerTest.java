package Raiffeisen.Onboarding;

import Raiffeisen.Onboarding.Controller.AuthenticationController;
import Raiffeisen.Onboarding.Entities.User;
import Raiffeisen.Onboarding.JWT.dtos.*;
import Raiffeisen.Onboarding.JWT.services.*;
import Raiffeisen.Onboarding.JWT.configs.SecurityConfiguration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationController.class)
@Import(SecurityConfiguration.class)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String SIGNUP_URL = "/auth/signup";
    private static final String LOGIN_URL = "/auth/login";

    private RegisterUserDto validRegisterUserDto;
    private RegisterUserDto invalidRegisterUserDto;
    private LoginUserDto validLoginUserDto;
    private User mockUser;

    @BeforeEach
    void setUp() {
        validRegisterUserDto = createRegisterUserDto("testuser", "password123");
        invalidRegisterUserDto = createRegisterUserDto("", "");
        validLoginUserDto = createLoginUserDto("testuser", "password123");
        mockUser = createMockUser();
    }

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        Mockito.when(authenticationService.signup(any(RegisterUserDto.class))).thenReturn(mockUser);

        mockMvc.perform(post(SIGNUP_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRegisterUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockUser.getId()))
                .andExpect(jsonPath("$.username").value(mockUser.getUsername()));
    }

    @Test
    void shouldAuthenticateUserSuccessfully() throws Exception {
        String mockToken = "mock-jwt-token";
        long mockExpirationTime = 3600L;

        Mockito.when(authenticationService.authenticate(any(LoginUserDto.class))).thenReturn(mockUser);
        Mockito.when(jwtService.generateToken(mockUser)).thenReturn(mockToken);
        Mockito.when(jwtService.getExpirationTime()).thenReturn(mockExpirationTime);

        mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoginUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(mockToken))
                .andExpect(jsonPath("$.expiresIn").value(mockExpirationTime));
    }

    /*@Test
    void shouldReturnBadRequestWhenRegisterDataIsInvalid() throws Exception {
        mockMvc.perform(post(SIGNUP_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRegisterUserDto)))
                .andExpect(status().isBadRequest());
    }*/

    @Test
    void shouldReturnInternalServerErrorWhenAuthenticationFails() throws Exception {
        Mockito.when(authenticationService.authenticate(any(LoginUserDto.class)))
                .thenThrow(new RuntimeException("Authentication failed"));

        mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoginUserDto)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.detail").value("Authentication failed"))
                .andExpect(jsonPath("$.status").value(500));
    }

    private RegisterUserDto createRegisterUserDto(String username, String password) {
        RegisterUserDto dto = new RegisterUserDto();
        dto.setUsername(username);
        dto.setPassword(password);
        return dto;
    }

    private LoginUserDto createLoginUserDto(String username, String password) {
        LoginUserDto dto = new LoginUserDto();
        dto.setUsername(username);
        dto.setPassword(password);
        return dto;
    }

    private User createMockUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setId(String.valueOf(1L));
        return user;
    }
}
