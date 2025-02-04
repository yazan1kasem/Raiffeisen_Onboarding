package Raiffeisen.Onboarding.ExcelGenerator;

import Raiffeisen.Onboarding.Entities.CheckList;
import Raiffeisen.Onboarding.Entities.Item;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelGeneratorService {

    public void generateChecklistExcel(CheckList checklist, ByteArrayOutputStream outputStream) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Checklisten Report");

        // Header erstellen
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("#");
        headerRow.createCell(1).setCellValue("Name");
        headerRow.createCell(2).setCellValue("Typ");

        int rowIndex = 1;
        for (Item item : checklist.getItems()) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(rowIndex - 1);
            row.createCell(1).setCellValue(item.getName());
            row.createCell(2).setCellValue(item.getType());
        }

        workbook.write(outputStream);
        workbook.close();
    }
}
