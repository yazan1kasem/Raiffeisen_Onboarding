package Raiffeisen.Onboarding.PDFGenerator;

import Raiffeisen.Onboarding.Entities.CheckList;
import Raiffeisen.Onboarding.Entities.Item;
import Raiffeisen.Onboarding.Entities.User_Checklist_Items;
import Raiffeisen.Onboarding.Entities.User_Checklists;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;


import org.apache.poi.xwpf.usermodel.*;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;


@Service
public class PdfGeneratorService {

    public void generateChecklistPdf(User_Checklists checklist, ByteArrayOutputStream outputStream) throws IOException {
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        document.add(new Paragraph("Checklisten Report").setBold().setFontSize(18));

        document.add(new Paragraph("Abteilung: " + checklist.getOriginalChecklist().getAbteilungsname()));
        document.add(new Paragraph("Position: " + checklist.getOriginalChecklist().getPosition()));

        // Tabelle für Items erstellen
        Table table = new Table(new float[]{1, 3, 3});
        table.addCell(new Cell().add(new Paragraph("#")));
        table.addCell(new Cell().add(new Paragraph("Name")));
        table.addCell(new Cell().add(new Paragraph("Typ")));

        int index = 1;
        for (User_Checklist_Items item : checklist.getUseritems()) {
            table.addCell(new Cell().add(new Paragraph(String.valueOf(index++))));
            table.addCell(new Cell().add(new Paragraph(item.getOriginalItem().getName())));
            table.addCell(new Cell().add(new Paragraph(item.getOriginalItem().getType())));

            Cell cell = new Cell();
            if (item.isChecked()) {
                cell.add(new Paragraph("☑"));
            } else {
                cell.add(new Paragraph("☐"));
            }
            table.addCell(cell);
        }

        document.add(table);
        document.close();
    }
}
