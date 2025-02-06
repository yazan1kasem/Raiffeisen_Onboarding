package Raiffeisen.Onboarding;

import Raiffeisen.Onboarding.Controller.UserController;
import Raiffeisen.Onboarding.Entities.User;
import Raiffeisen.Onboarding.JWT.dtos.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        // Mock User setup
        mockUser = new User();
        mockUser.setUsername("testuser");
        mockUser.setPassword("password");
        mockUser.setEnabled(true);
        mockUser.setRole(User.Role.USER); // Set role to USER

        // Lenient stubbing für unbenutzte Stubbings
        Authentication authentication = org.mockito.Mockito.mock(Authentication.class);
        lenient().when(authentication.getPrincipal()).thenReturn(mockUser);
        SecurityContext securityContext = org.mockito.Mockito.mock(SecurityContext.class);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void shouldReturnAuthenticatedUser() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(mockUser.getUsername()))
                .andExpect(jsonPath("$.enabled").value(mockUser.isEnabled()))
                .andExpect(jsonPath("$.role").value(mockUser.getRole().name()));
    }

    @Test
    void shouldReturnAllUsers() throws Exception {
        // Mocked response from the service layer
        List<User> users = Arrays.asList(mockUser);
        when(userService.allUsers()).thenReturn(users);

        mockMvc.perform(get("/users/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value(mockUser.getUsername()))
                .andExpect(jsonPath("$[0].enabled").value(mockUser.isEnabled()))
                .andExpect(jsonPath("$[0].role").value(mockUser.getRole().name()));
    }
}