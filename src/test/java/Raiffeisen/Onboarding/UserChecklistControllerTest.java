package Raiffeisen.Onboarding;

import Raiffeisen.Onboarding.Controller.UserChecklistController;
import Raiffeisen.Onboarding.Entities.User_Checklists;
import Raiffeisen.Onboarding.Repository.UserChecklistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserChecklistControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserChecklistRepository userChecklistRepository;

    @InjectMocks
    private UserChecklistController userChecklistController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userChecklistController).build();
    }

    @Test
    void getAllUserChecklists() throws Exception {
        // Arrange
        User_Checklists userChecklist1 = new User_Checklists();
        User_Checklists userChecklist2 = new User_Checklists();
        Iterable<User_Checklists> allUserChecklists = List.of(userChecklist1, userChecklist2);
        when(userChecklistRepository.findAll()).thenReturn(allUserChecklists);

        // Act & Assert
        mockMvc.perform(get("/userchecklist"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[1]").exists());
    }

    @Test
    void getUserChecklistById() throws Exception {
        // Arrange
        String id = "1";
        User_Checklists userChecklist = new User_Checklists();
        when(userChecklistRepository.findById(id)).thenReturn(Optional.of(userChecklist));

        // Act & Assert
        mockMvc.perform(get("/userchecklist/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getUserChecklistById_NotFound() throws Exception {
        // Arrange
        String id = "1";
        when(userChecklistRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/userchecklist/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUserChecklist() throws Exception {
        // Arrange
        User_Checklists newUserChecklist = new User_Checklists();
        when(userChecklistRepository.save(any(User_Checklists.class))).thenReturn(newUserChecklist);

        // Act & Assert
        mockMvc.perform(post("/userchecklist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ueberschrift\":\"Test Checklist\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void updateUserChecklist() throws Exception {
        // Arrange
        String id = "1";
        User_Checklists existingUserChecklist = new User_Checklists();
        User_Checklists updatedUserChecklist = new User_Checklists();
        when(userChecklistRepository.findById(id)).thenReturn(Optional.of(existingUserChecklist));
        when(userChecklistRepository.save(any(User_Checklists.class))).thenReturn(updatedUserChecklist);

        // Act & Assert
        mockMvc.perform(put("/userchecklist/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ueberschrift\":\"Updated Checklist\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void updateUserChecklist_NotFound() throws Exception {
        // Arrange
        String id = "1";
        when(userChecklistRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(put("/userchecklist/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ueberschrift\":\"Updated Checklist\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUserChecklist() throws Exception {
        // Arrange
        String id = "1";
        when(userChecklistRepository.existsById(id)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/userchecklist/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUserChecklist_NotFound() throws Exception {
        // Arrange
        String id = "1";
        when(userChecklistRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(delete("/userchecklist/{id}", id))
                .andExpect(status().isNotFound());
    }
}
