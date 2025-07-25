package edu.yacoubi.tasks.services.export;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PdfExportService<T> implements IExportService<T> {

    private final MessageSource messageSource;

    @Override
    public byte[] exportAsPdf(List<T> items) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, stream);
        document.open();

        // Titel des Dokuments
        // Beispiel: Dynamisch Locale setzen
        Locale locale = LocaleContextHolder.getLocale(); // Holt aktuelle Locale aus dem Thread-Context (z.B. für Webanwendung)
        String title = getLocalizedHeader("export.pdf.title", locale);


        document.add(new Paragraph(title));
        document.add(new Paragraph(getLocalizedHeader("export.pdf.description", locale)));
        document.add(new Paragraph(getLocalizedHeader("export.pdf.timestamp", locale) + ": " + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))));


        //document.add(new Paragraph("Exportiert am: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))));
        document.add(new Paragraph(" ")); // Leerzeile für bessere Lesbarkeit

        if (items == null || items.isEmpty()) {
            document.add(new Paragraph(getLocalizedHeader("export.pdf.empty", locale)));
            document.close();
            return stream.toByteArray();
        }

        Class<?> clazz = items.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();

        // Tabelle mit Reflection erzeugen
        PdfPTable table = new PdfPTable(fields.length);
        table.setWidthPercentage(100); // füllt die Seite besser
        table.setWidths(new float[]{2f, 3f, 4f, 2f, 2f, 1f, 1f});


        // Spaltenüberschriften
        for (Field field : fields) {
            field.setAccessible(true);

            // 🪄 Lesbaren Titel erzeugen
            //String label = field.getName().replaceAll("([a-z])([A-Z])", "$1 $2"); // z.B. "taskTitle" → "task Title"

            String label = StringUtils.capitalize(
                    field.getName().replaceAll("([a-z])([A-Z])", "$1 $2")
            );


            PdfPCell header = new PdfPCell(new Phrase(label));
            header.setBackgroundColor(Color.LIGHT_GRAY);
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(header);
        }


        // Datenzeilen
//        for (T item : items) {
//            for (Field field : fields) {
//                field.setAccessible(true);
//                try {
//                    Object value = field.get(item);
//                    if (value instanceof LocalDate) {
//                        value = ((LocalDate) value).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
//                    } else if (value instanceof LocalDateTime) {
//                        value = ((LocalDateTime) value).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
//                    }
//                    table.addCell(value != null ? value.toString() : "");
//                } catch (IllegalAccessException e) {
//                    table.addCell("Zugriffsfehler");
//                }
//            }
//        }
        boolean isEven = true; // Hilfsvariable zur Unterscheidung der Zeilen (für Zebra-Stil)

        for (T item : items) {
            isEven = !isEven; // Bei jeder neuen Zeile wird isEven umgeschaltet

            for (Field field : fields) {
                field.setAccessible(true); // Zugriff auf private Felder erlauben
                try {
                    Object value = field.get(item); // Wert aus dem Feld des aktuellen Objekts holen

                    // Formatierung für Datum
                    if (value instanceof LocalDate) {
                        value = ((LocalDate) value).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                    }
                    // Formatierung für Zeitstempel
                    else if (value instanceof LocalDateTime) {
                        value = ((LocalDateTime) value).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                    }

                    // Zelle mit Wert erstellen
                    PdfPCell cell = new PdfPCell(new Phrase(value != null ? value.toString() : ""));

                    // Hintergrundfarbe setzen, wenn gerade Zeile (Zebra-Stil)
                    if (isEven) {
                        cell.setBackgroundColor(new Color(230, 230, 230)); // Hellgrau
                    }

                    table.addCell(cell); // Zelle zur Tabelle hinzufügen

                } catch (IllegalAccessException e) {
                    // Fehlerzelle, falls Feld nicht zugreifbar ist
                    PdfPCell errorCell = new PdfPCell(new Phrase("Zugriffsfehler"));

                    // Auch hier Zebra-Stil anwenden
                    if (isEven) {
                        errorCell.setBackgroundColor(new Color(230, 230, 230));
                    }

                    table.addCell(errorCell); // Fehlerzelle zur Tabelle hinzufügen
                }
            }
        }


        document.add(table);
        document.close();
        return stream.toByteArray();
    }

    @Override
    public void exportPdfToFile(List<T> items, String targetPath) {
        byte[] pdfBytes = exportAsPdf(items);
        String path = "export_" + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm")) + ".pdf";

        try (FileOutputStream fos = new FileOutputStream(path)) {
            fos.write(pdfBytes);
        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Schreiben der PDF-Datei: " + targetPath, e);
        }
    }

    @Override
    public byte[] exportAsCsv(List<T> items) {
        // Implementiere die Logik zum Exportieren der Items als CSV
        return new byte[0]; // Platzhalter
    }

    @Override
    public void exportCsvToFile(List<T> items, String targetPath) {
        // Implementiere die Logik zum Speichern der Items als CSV in eine Datei
    }

    private String getLocalizedHeader(String key, Locale locale) {
        return messageSource.getMessage(key, null, locale);
    }
}
