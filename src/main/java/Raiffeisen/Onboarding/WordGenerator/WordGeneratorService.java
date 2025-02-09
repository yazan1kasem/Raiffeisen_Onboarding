package Raiffeisen.Onboarding.WordGenerator;

import Raiffeisen.Onboarding.Entities.CheckList;
import Raiffeisen.Onboarding.Entities.Item;
import Raiffeisen.Onboarding.Entities.User_Checklist_Items;
import Raiffeisen.Onboarding.Entities.User_Checklists;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class WordGeneratorService {

    public void generateChecklistWord(User_Checklists checklist, ByteArrayOutputStream outputStream) throws IOException {
        XWPFDocument document = new XWPFDocument();

        // Titel hinzufügen
        XWPFParagraph title = document.createParagraph();
        XWPFRun titleRun = title.createRun();
        titleRun.setText("Checklisten Report");
        titleRun.setBold(true);
        titleRun.setFontSize(18);

        // Überschrift der Checkliste
        XWPFParagraph checklistTitle = document.createParagraph();
        XWPFRun checklistRun = checklistTitle.createRun();

        checklistRun.setBold(true);
        checklistRun.setFontSize(14);

        // Details hinzufügen
        XWPFParagraph details = document.createParagraph();
        details.createRun().setText("Abteilung: " + checklist.getOriginalChecklist().getAbteilungsname());
        details.createRun().addBreak();
        details.createRun().setText("Position: " + checklist.getOriginalChecklist().getPosition());

        // Tabelle für Items erstellen
        XWPFTable table = document.createTable();

        // Headerzeile
        XWPFTableRow headerRow = table.getRow(0);
        headerRow.getCell(0).setText("#");
        headerRow.addNewTableCell().setText("Name");
        headerRow.addNewTableCell().setText("Typ");

        // Items hinzufügen
        int index = 1;
        for (User_Checklist_Items item : checklist.getUseritems()) {
            XWPFTableRow row = table.createRow();
            row.getCell(0).setText(String.valueOf(index++));
            row.getCell(1).setText(item.getOriginalItem().getName());
            row.getCell(2).setText(item.getOriginalItem().getType());

            XWPFTableCell cell = row.addNewTableCell();
            if (item.isChecked()) {
                cell.setText("☑");
            } else {
                cell.setText("☐");
            }
        }

        document.write(outputStream);
        document.close();
    }
}
