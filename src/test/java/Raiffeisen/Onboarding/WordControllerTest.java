package Raiffeisen.Onboarding;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import Raiffeisen.Onboarding.Controller.WordController;
import Raiffeisen.Onboarding.Entities.CheckList;
import Raiffeisen.Onboarding.Entities.Item;
import Raiffeisen.Onboarding.WordGenerator.WordGeneratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

@SpringBootTest
class WordControllerTest {

    @InjectMocks
    private WordController wordController;

    @Mock
    private WordGeneratorService wordGeneratorService;

    private CheckList checklist;

    @BeforeEach
    void setUp() {
        checklist = new CheckList();
        checklist.setAbteilungsname("Abteilung A");
        checklist.setPosition("Position 1");

        // Beispiel-Items erstellen
        Item item1 = new Item("Item 1", "Type A","Suchbegriff 1");
        Item item2 = new Item("Item 2", "Type B","Suchbegriff 2");

        checklist.setItems(Arrays.asList(item1, item2));
    }

    @Test
    void testGenerateChecklistWord() throws IOException {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("Word content".getBytes()); // Simulate Word content

        // Mock der wordGeneratorService Methode
        doAnswer(invocation -> {
            ByteArrayOutputStream baos = invocation.getArgument(1);
            baos.write("Word content".getBytes());
            return null;
        }).when(wordGeneratorService).generateChecklistWord(eq(checklist), any(ByteArrayOutputStream.class));

        // Act
        ResponseEntity<byte[]> response = wordController.generateChecklistWord(checklist);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode()); // Überprüfen, dass der Statuscode OK ist

        // Header überprüfen
        assertTrue(response.getHeaders().containsKey(HttpHeaders.CONTENT_DISPOSITION));
        assertTrue(response.getHeaders().get(HttpHeaders.CONTENT_DISPOSITION).get(0).contains("checklist_report.docx"));

        // Überprüfen, dass der Inhalt des Antwortkörpers nicht null oder leer ist
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
    }
}