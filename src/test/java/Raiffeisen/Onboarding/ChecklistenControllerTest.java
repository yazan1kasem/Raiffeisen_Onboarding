package Raiffeisen.Onboarding;

import Raiffeisen.Onboarding.Controller.CheckListenController;
import Raiffeisen.Onboarding.Entities.CheckList;
import Raiffeisen.Onboarding.Entities.Device;
import Raiffeisen.Onboarding.Entities.Department;
import Raiffeisen.Onboarding.Entities.Position;
import Raiffeisen.Onboarding.Entities.User;
import Raiffeisen.Onboarding.JWT.configs.JwtAuthenticationFilter;
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
    private CheckListenRepository checkListenRepository;

    @MockBean
    private JwtService jwtService; // Mock hinzugefügt für JwtService

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private CheckList checkList;

    @BeforeEach
    void setUp() {
        checkList = buildCheckList();
    }

    // Hilfsmethode zur Erstellung von Testdaten
    private CheckList buildCheckList() {
        Device device = new Device();
        device.setId("d1");
        device.setGeraet("Test Device");

        Department department = new Department();
        department.setId("dep1");
        department.setGeraet("Test Geraet");

        Position position = new Position();
        position.setId("p1");
        position.setBezeichnung("Test Position");

        User user = new User();
        user.setId("u1");
        user.setUsername("testUser");

        CheckList checkList = new CheckList();
        checkList.setId("1");
        checkList.setUeberschrift("Test Ueberschrift");
        checkList.setDevice(device);
        checkList.setDepartment(department);
        checkList.setPosition(position);
        checkList.setUsers(Set.of(user));
        checkList.setSaved(true);
        return checkList;
    }

    @Test
    void testGetAllCheckListen() throws Exception {
        Mockito.when(checkListenRepository.findAll()).thenReturn(java.util.List.of(checkList));

        mockMvc.perform(get("/checklisten"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ueberschrift").value("Test Ueberschrift"))
                .andExpect(jsonPath("$[0].device.geraet").value("Test Device"))
                .andExpect(jsonPath("$[0].department.geraet").value("Test Geraet"))
                .andExpect(jsonPath("$[0].position.bezeichnung").value("Test Position"));
    }

    @Test
    void testGetCheckListenById() throws Exception {
        Mockito.when(checkListenRepository.findById("1")).thenReturn(Optional.of(checkList));

        mockMvc.perform(get("/checklisten/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ueberschrift").value("Test Ueberschrift"))
                .andExpect(jsonPath("$.device.geraet").value("Test Device"))
                .andExpect(jsonPath("$.department.geraet").value("Test Geraet"))
                .andExpect(jsonPath("$.position.bezeichnung").value("Test Position"));
    }

    @Test
    void testGetCheckListenById_NotFound() throws Exception {
        Mockito.when(checkListenRepository.findById("999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/checklisten/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateCheckListen() throws Exception {
        Mockito.when(checkListenRepository.save(any(CheckList.class))).thenReturn(checkList);

        mockMvc.perform(post("/checklisten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(checkList)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ueberschrift").value("Test Ueberschrift"))
                .andExpect(jsonPath("$.device.geraet").value("Test Device"))
                .andExpect(jsonPath("$.department.geraet").value("Test Geraet"))
                .andExpect(jsonPath("$.position.bezeichnung").value("Test Position"));
    }

    @Test
    void testUpdateCheckListen() throws Exception {
        Mockito.when(checkListenRepository.save(any(CheckList.class))).thenReturn(checkList);

        mockMvc.perform(put("/checklisten/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(checkList)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ueberschrift").value("Test Ueberschrift"))
                .andExpect(jsonPath("$.device.geraet").value("Test Device"))
                .andExpect(jsonPath("$.department.geraet").value("Test Geraet"))
                .andExpect(jsonPath("$.position.bezeichnung").value("Test Position"));
    }
}
