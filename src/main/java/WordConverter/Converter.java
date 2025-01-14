package WordConverter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class Converter {

    /**
     * Methode zum Erstellen eines Word-Dokuments.
     *
     * @param filePath Der Pfad und Name der Datei, die erstellt werden soll.
     * @return true, wenn das Dokument erfolgreich erstellt wurde; false bei Fehlern.
     */
    public boolean createWordDocument(String filePath) {
        // Erstelle ein leeres Word-Dokument
        try (XWPFDocument document = new XWPFDocument();
             OutputStream fileOut = new FileOutputStream(filePath)) {

            // Schreibe das leere Dokument in die Datei
            document.write(fileOut);
            System.out.println("Word-Dokument wurde erfolgreich erstellt: " + filePath);
            return true;

        } catch (IOException e) {
            System.err.println("Fehler beim Erstellen des Word-Dokuments: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        // Beispiel für die Verwendung der Methode
        Converter converter = new Converter();
        String filePath = "Javatpoint.docx";

        if (converter.createWordDocument(filePath)) {
            System.out.println("Dokument wurde erfolgreich erstellt.");
        } else {
            System.out.println("Dokumenterstellung fehlgeschlagen.");
        }
    }
}
