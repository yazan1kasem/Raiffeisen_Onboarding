package Raiffeisen.Onboarding;

import Raiffeisen.Onboarding.Controller.AuthenticationController;
import Raiffeisen.Onboarding.Entities.User;
import Raiffeisen.Onboarding.JWT.dtos.LoginResponse;
import Raiffeisen.Onboarding.JWT.dtos.LoginUserDto;
import Raiffeisen.Onboarding.JWT.dtos.RegisterUserDto;
import Raiffeisen.Onboarding.JWT.services.AuthenticationService;
import Raiffeisen.Onboarding.JWT.services.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthenticationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    void testRegister() throws Exception {
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setUsername("testuser");
        registerUserDto.setPassword("password123");

        User mockUser = new User();
        mockUser.setUsername("testuser");
        mockUser.setPassword("password123");
        mockUser.setRole(User.Role.USER);

        when(authenticationService.signup(any(RegisterUserDto.class))).thenReturn(mockUser);

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"username\": \"testuser\", \"password\": \"password123\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void testAuthenticate() throws Exception {
        LoginUserDto loginUserDto = new LoginUserDto();
        User mockUser = new User();
        mockUser.setRole(User.Role.USER);

        when(authenticationService.authenticate(any(LoginUserDto.class))).thenReturn(mockUser);
        when(jwtService.generateToken(mockUser)).thenReturn("mockToken");
        when(jwtService.getExpirationTime()).thenReturn(3600L);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mockToken"))
                .andExpect(jsonPath("$.expiresIn").value(3600))
                .andExpect(jsonPath("$.role").value("USER"));
    }
}