package Raiffeisen.Onboarding;

import Raiffeisen.Onboarding.Controller.AdminController;
import Raiffeisen.Onboarding.Entities.CheckList;
import Raiffeisen.Onboarding.Entities.Item;
import Raiffeisen.Onboarding.Entities.User;
import Raiffeisen.Onboarding.Entities.User_Checklists;
import Raiffeisen.Onboarding.JWT.dtos.LoginUserDto;
import Raiffeisen.Onboarding.Repository.CheckListenRepository;
import Raiffeisen.Onboarding.Repository.ItemRepository;
import Raiffeisen.Onboarding.Repository.UserChecklistRepository;
import Raiffeisen.Onboarding.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AdminControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserChecklistRepository userChecklistRepository;

    @Mock
    private CheckListenRepository checkListenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminController adminController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    public void testUpdateCheckListen() throws Exception {
        // Given
        String checklistId = "test-id";
        CheckList checkList = new CheckList(checklistId, "Position", "Dept", null);
        CheckList updatedCheckList = new CheckList(checklistId, "Updated Position", "Updated Dept", null);

        when(checkListenRepository.findById(checklistId)).thenReturn(Optional.of(checkList));
        when(checkListenRepository.save(any(CheckList.class))).thenReturn(updatedCheckList);

        // When & Then
        mockMvc.perform(put("/admin/checklist")
                        .contentType("application/json")
                        .content("{\"id\":\"test-id\",\"position\":\"Updated Position\",\"abteilungsname\":\"Updated Dept\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.position").value("Updated Position"))
                .andExpect(jsonPath("$.abteilungsname").value("Updated Dept"));

        verify(checkListenRepository, times(1)).findById(checklistId);
        verify(checkListenRepository, times(1)).save(any(CheckList.class));
    }

/*
    @Test
    public void testChangeUserPassword() throws Exception {
        // Given
        String userId = "user-id";
        User user = new User(userId, "oldPassword");
        user.setRole(User.Role.USER); // Set the role to avoid null pointer exception
        User updatedUser = new User(userId, "encodedNewPassword");
        updatedUser.setRole(User.Role.USER); // Set the role to avoid null pointer exception

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // When & Then
        mockMvc.perform(put("/admin/user/{id}", userId)
                        .param("password", "newPassword"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.password").value("encodedNewPassword"));

        verify(userRepository, times(1)).findById(userId);
        verify(passwordEncoder, times(1)).encode("newPassword");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testGetAllUserChecklists() throws Exception {
        // Given
        User_Checklists userChecklist = new User_Checklists();
        when(userChecklistRepository.findAll()).thenReturn(Arrays.asList(userChecklist));

        // When & Then
        mockMvc.perform(get("/admin/userchecklist"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1)); // Assert that the length of the array is 1

        verify(userChecklistRepository, times(1)).findAll();
    }
*/
    @Test
    public void testGetAllItems() throws Exception {
        // Given
        Item item = new Item();
        when(itemRepository.findAll()).thenReturn(Arrays.asList(item));

        // When & Then
        mockMvc.perform(get("/admin/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1)); // Assert that the length of the array is 1

        verify(itemRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllUsers() throws Exception {
        // Given
        User user1 = new User();
        user1.setUsername("user1");
        user1.setRole(User.Role.USER); // Set the role to avoid null pointer exception

        User user2 = new User();
        user2.setUsername("user2");
        user2.setRole(User.Role.ADMIN); // Set the role to avoid null pointer exception

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // When & Then
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2)) // Assert that the length of the array is 2
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[1].username").value("user2"));

        verify(userRepository, times(1)).findAll();
    }
}