package Raiffeisen.Onboarding.Controller;

import Raiffeisen.Onboarding.Entities.CheckList;
import Raiffeisen.Onboarding.ExcelGenerator.ExcelGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/excel")
public class ExcelController {

    @Autowired
    private ExcelGeneratorService excelGeneratorService;

    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateChecklistExcel(@RequestBody CheckList checklist) {
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
}
