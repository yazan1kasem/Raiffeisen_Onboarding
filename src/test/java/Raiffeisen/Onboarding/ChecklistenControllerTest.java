package Raiffeisen.Onboarding;

import Raiffeisen.Onboarding.Controller.CheckListenController;
import Raiffeisen.Onboarding.Entities.CheckList;
import Raiffeisen.Onboarding.Entities.Item;
import Raiffeisen.Onboarding.Entities.User;
import Raiffeisen.Onboarding.JWT.services.JwtService;
import Raiffeisen.Onboarding.Repository.CheckListenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CheckListenController.class)
class ChecklistenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CheckListenRepository checkListenRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private CheckList checkList;

    @BeforeEach
    void setUp() {
        checkList = buildCheckList();
    }

    private CheckList buildCheckList() {
        Item item1 = new Item();
        item1.setId("i1");
        item1.setGeraet("Test Gerät 1");
        item1.setAdministration("Test Administration 1");
        item1.setSoftware("Test Software 1");
        item1.setSuchbegriff("Test Suchbegriff 1");

        Item item2 = new Item();
        item2.setId("i2");
        item2.setGeraet("Test Gerät 2");
        item2.setAdministration("Test Administration 2");
        item2.setSoftware("Test Software 2");
        item2.setSuchbegriff("Test Suchbegriff 2");

        CheckList checkList = new CheckList();
        checkList.setId("1");
        checkList.setUeberschrift("Test Ueberschrift");
        checkList.setAbteilungsname("Test Abteilung");
        checkList.setPosition("Test Position");
        checkList.setItems(List.of(item1, item2));

        return checkList;
    }


    @Test
    void testGetAllCheckListen() throws Exception {
        Mockito.when(checkListenRepository.findAll()).thenReturn(List.of(checkList));

        mockMvc.perform(get("/checklisten"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ueberschrift").value("Test Ueberschrift"))
                .andExpect(jsonPath("$[0].abteilungsname").value("Test Abteilung"))
                .andExpect(jsonPath("$[0].position").value("Test Position"))
                .andExpect(jsonPath("$[0].items[0].name").value("Item 1"))
                .andExpect(jsonPath("$[0].items[1].name").value("Item 2"));
    }

    @Test
    void testGetCheckListenById() throws Exception {
        Mockito.when(checkListenRepository.findById("1")).thenReturn(Optional.of(checkList));

        mockMvc.perform(get("/checklisten/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ueberschrift").value("Test Ueberschrift"))
                .andExpect(jsonPath("$.abteilungsname").value("Test Abteilung"))
                .andExpect(jsonPath("$.position").value("Test Position"))
                .andExpect(jsonPath("$.items[0].name").value("Item 1"))
                .andExpect(jsonPath("$.items[1].name").value("Item 2"));
    }

    @Test
    void testCreateCheckListen() throws Exception {
        Mockito.when(checkListenRepository.save(any(CheckList.class))).thenReturn(checkList);

        mockMvc.perform(post("/checklisten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(checkList)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ueberschrift").value("Test Ueberschrift"))
                .andExpect(jsonPath("$.abteilungsname").value("Test Abteilung"))
                .andExpect(jsonPath("$.position").value("Test Position"))
                .andExpect(jsonPath("$.items[0].name").value("Item 1"))
                .andExpect(jsonPath("$.items[1].name").value("Item 2"));
    }
}
