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

        mockCheckList = new CheckList();
        mockCheckList.setId("1");
        mockCheckList.setUeberschrift("Test CheckList");

        Item item1 = new Item();
        item1.setGeraet("Item1");
        Item item2 = new Item();
        item2.setGeraet("Item2");

        mockCheckList.setItems(Arrays.asList(item1, item2));
    }

    @Test
    void shouldReturnAllCheckListen() throws Exception {
        when(checkListenRepository.findAll()).thenReturn(Arrays.asList(mockCheckList));

        mockMvc.perform(get("/checklisten"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(mockCheckList.getId()))
                .andExpect(jsonPath("$[0].ueberschrift").value(mockCheckList.getUeberschrift()));
    }

    @Test
    void shouldReturnCheckListById() throws Exception {
        when(checkListenRepository.findById("1")).thenReturn(Optional.of(mockCheckList));

        mockMvc.perform(get("/checklisten/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockCheckList.getId()))
                .andExpect(jsonPath("$.ueberschrift").value(mockCheckList.getUeberschrift()));
    }

    @Test
    void shouldReturnNotFoundForNonExistentCheckList() throws Exception {
        when(checkListenRepository.findById(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/checklisten/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateNewCheckList() throws Exception {
        when(checkListenRepository.save(any(CheckList.class))).thenReturn(mockCheckList);

        mockMvc.perform(post("/checklisten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockCheckList)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockCheckList.getId()))
                .andExpect(jsonPath("$.ueberschrift").value(mockCheckList.getUeberschrift()));
    }

    @Test
    void shouldUpdateExistingCheckList() throws Exception {
        when(checkListenRepository.findById("1")).thenReturn(Optional.of(mockCheckList));
        when(checkListenRepository.save(any(CheckList.class))).thenReturn(mockCheckList);

        CheckList updatedCheckList = new CheckList();
        updatedCheckList.setUeberschrift("Updated CheckList");
        updatedCheckList.setItems(Arrays.asList(
                Item.builder().geraet("NewItem1").build(),
                Item.builder().geraet("NewItem2").build()
        ));

        mockMvc.perform(put("/checklisten/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCheckList)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ueberschrift").value("Updated CheckList"))
                .andExpect(jsonPath("$.items[0].geraet").value("NewItem1"));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentCheckList() throws Exception {
        when(checkListenRepository.findById(anyString())).thenReturn(Optional.empty());

        CheckList updatedCheckList = new CheckList();
        updatedCheckList.setUeberschrift("Updated CheckList");

        mockMvc.perform(put("/checklisten/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCheckList)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteCheckListById() throws Exception {
        when(checkListenRepository.existsById("1")).thenReturn(true);

        mockMvc.perform(delete("/checklisten/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentCheckList() throws Exception {
        when(checkListenRepository.existsById(anyString())).thenReturn(false);

        mockMvc.perform(delete("/checklisten/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldSearchCheckListByTitle() throws Exception {
        when(checkListenRepository.findByUeberschriftContaining("Test"))
                .thenReturn(Arrays.asList(mockCheckList));

        mockMvc.perform(get("/checklisten/checklisten/search")
                        .param("title", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ueberschrift").value("Test CheckList"));
    }
}