package Raiffeisen.Onboarding.Controller;

import Raiffeisen.Onboarding.PDFGenerator.PdfGeneratorService;
import Raiffeisen.Onboarding.Entities.CheckList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    @Autowired
    private PdfGeneratorService pdfGeneratorService;

    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateChecklistPdf(@RequestBody CheckList checklist) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            pdfGeneratorService.generateChecklistPdf(checklist, outputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=checklist_report.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                    .body(outputStream.toByteArray());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
