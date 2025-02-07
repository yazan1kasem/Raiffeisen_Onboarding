package Raiffeisen.Onboarding;

import Raiffeisen.Onboarding.Controller.ExcelController;
import Raiffeisen.Onboarding.Entities.CheckList;
import Raiffeisen.Onboarding.Entities.Item;
import Raiffeisen.Onboarding.ExcelGenerator.ExcelGeneratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ExcelControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ExcelGeneratorService excelGeneratorService;

    @InjectMocks
    private ExcelController excelController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(excelController).build();
    }

    @Test
    void testGenerateChecklistExcel() throws Exception {
        // Arrange: Erstellen von Mock-Daten für CheckList und Item
        CheckList checklist = new CheckList();
        Item item1 = new Item("Item 1", "Type 1", "SearchTerm 1");
        checklist.setItems(Arrays.asList(item1));

        // Mock der Service-Methode, die das Excel generiert
        ByteArrayOutputStream mockOutputStream = new ByteArrayOutputStream();
        mockMvc.perform(post("/api/excel/generate")
                        .contentType("application/json")
                        .content("{\"items\": [{\"name\": \"Item 1\", \"type\": \"Type 1\"}]}"))
                .andExpect(status().isOk())  // Erwartet 200 OK
                .andExpect(header().string("Content-Disposition", "attachment; filename=checklist_report.xlsx"))
                .andExpect(content().contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
    }
}
