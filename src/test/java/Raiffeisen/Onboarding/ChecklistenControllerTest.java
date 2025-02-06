package Raiffeisen.Onboarding;

import Raiffeisen.Onboarding.Controller.CheckListenController;
import Raiffeisen.Onboarding.Entities.CheckList;
import Raiffeisen.Onboarding.Entities.Item;
import Raiffeisen.Onboarding.Repository.CheckListenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ChecklistenControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CheckListenRepository checkListenRepository;

    @InjectMocks
    private CheckListenController checkListenController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private CheckList mockCheckList;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(checkListenController).build();

        // Mock CheckList setup with UUID for id
        mockCheckList = new CheckList();
        mockCheckList.setId("1"); // Use a predefined UUID for mock
        mockCheckList.setPosition("Position 1");
        mockCheckList.setAbteilungsname("Department 1");

        // Set up mock items
        Item item1 = new Item();
        item1.setType("NewItem1"); // Mock item field
        Item item2 = new Item();
        item2.setType("NewItem2");

        mockCheckList.setItems(Arrays.asList(item1, item2));
    }

    @Test
    void shouldReturnAllCheckListen() throws Exception {
        when(checkListenRepository.findAll()).thenReturn(Arrays.asList(mockCheckList));

        mockMvc.perform(get("/checklisten"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(mockCheckList.getId()))
                .andExpect(jsonPath("$[0].position").value(mockCheckList.getPosition()))
                .andExpect(jsonPath("$[0].abteilungsname").value(mockCheckList.getAbteilungsname()))
                // Hier wird nach dem korrekten JSON-Pfad für den Item-Typ gesucht
                .andExpect(jsonPath("$[0].items[0].type").value("NewItem1")) // Wir erwarten den Typ des Items
                .andExpect(jsonPath("$[0].items[1].type").value("NewItem2")); // Den Typ des zweiten Items erwarten
    }


    @Test
    void shouldReturnCheckListById() throws Exception {
        // Erstellen der Item-Objekte mit den Namen "NewItem1" und "NewItem2"
        List<Item> items = Arrays.asList(
                new Item("1", "NewItem1", "type1", "search1"),  // Item 1
                new Item("2", "NewItem2", "type2", "search2")   // Item 2
        );

        // Erstellen der CheckList mit den entsprechenden Daten und den gemockten Item-Objekten
        CheckList mockCheckList = new CheckList("1", "position1", "abteilungsname1", items);

        // Mock-Verhalten des Repositories
        when(checkListenRepository.findById("1")).thenReturn(Optional.of(mockCheckList));

        // Durchführung des Tests: Überprüfen der zurückgegebenen JSON-Daten
        mockMvc.perform(get("/checklisten/1"))
                .andExpect(status().isOk())  // Erfolgreicher Status
                .andExpect(jsonPath("$.id").value(mockCheckList.getId()))  // Überprüfung der ID
                .andExpect(jsonPath("$.position").value(mockCheckList.getPosition()))  // Überprüfung der Position
                .andExpect(jsonPath("$.abteilungsname").value(mockCheckList.getAbteilungsname()))  // Überprüfung des Abteilungsnamens
                .andExpect(jsonPath("$.items[0].name").value("NewItem1"))  // Überprüfung des Namens des ersten Items
                .andExpect(jsonPath("$.items[1].name").value("NewItem2"));  // Überprüfung des Namens des zweiten Items
    }


    @Test
    void shouldReturnNotFoundForNonExistentCheckList() throws Exception {
        when(checkListenRepository.findById(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/checklisten/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentCheckList() throws Exception {
        when(checkListenRepository.existsById(anyString())).thenReturn(false);

        mockMvc.perform(delete("/checklisten/999"))
                .andExpect(status().isNotFound());
    }
}