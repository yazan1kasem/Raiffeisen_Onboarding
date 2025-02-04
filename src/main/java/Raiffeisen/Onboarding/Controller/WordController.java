package Raiffeisen.Onboarding.Controller;

import Raiffeisen.Onboarding.Entities.CheckList;
import Raiffeisen.Onboarding.WordGenerator.WordGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/word")
public class WordController {

    @Autowired
    private WordGeneratorService wordGeneratorService;

    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateChecklistWord(@RequestBody CheckList checklist) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            wordGeneratorService.generateChecklistWord(checklist, outputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=checklist_report.docx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(org.springframework.http.MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
                    .body(outputStream.toByteArray());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}