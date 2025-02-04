package Raiffeisen.Onboarding.Service;  // Paket angepasst

import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service  // Markiert die Klasse als Spring Bean
public class WordGenerator {

    public byte[] generateWordDocument() throws IOException {
        // Word-Dokument erstellen
        XWPFDocument document = new XWPFDocument();

        // Überschrift hinzufügen
        XWPFParagraph title = document.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleRun = title.createRun();
        titleRun.setText("Mein Word-Dokument");
        titleRun.setBold(true);
        titleRun.setFontSize(20);

        // Neuen Absatz mit Text
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun paragraphRun = paragraph.createRun();
        paragraphRun.setText("Das ist ein Beispieltext für ein Word-Dokument mit Apache POI.");
        paragraphRun.setFontSize(12);

        // Dokument als Byte-Array speichern
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.write(outputStream);
        document.close();

        return outputStream.toByteArray();
    }
}
