package Raiffeisen.Onboarding.Controller;

import Raiffeisen.Onboarding.Entities.User_Checklists;
import Raiffeisen.Onboarding.ExcelGenerator.ExcelGeneratorService;
import Raiffeisen.Onboarding.WordGenerator.WordGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ConverterController {
    @Autowired
    private WordGeneratorService wordGeneratorService;
    @Autowired
    private ExcelGeneratorService excelGeneratorService;

    @PostMapping("/word/generate")
    public ResponseEntity<byte[]> generateChecklistWord(@RequestBody User_Checklists checklist) {
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


    @PostMapping("/excel/generate")
    public ResponseEntity<byte[]> generateChecklistExcel(@RequestBody User_Checklists checklist) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            excelGeneratorService.generateChecklistExcel(checklist, outputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=checklist_report.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(org.springframework.http.MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(outputStream.toByteArray());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @Autowired
    private Raiffeisen.Onboarding.PDFGenerator.PdfGeneratorService pdfGeneratorService;

    @PostMapping("/pdf/generate")
    public ResponseEntity<byte[]> generateChecklistPdf(@RequestBody User_Checklists checklist) {
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
