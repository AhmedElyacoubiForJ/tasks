package edu.yacoubi.tasks.services.export;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PdfExportService<T> implements IExportService<T> {

    @Override
    public byte[] exportAsPdf(List<T> items) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, stream);
        document.open();

        if (items == null || items.isEmpty()) {
            document.add(new Paragraph("Keine Daten vorhanden."));
            document.close();
            return stream.toByteArray();
        }

        Class<?> clazz = items.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();

        // Tabelle mit Reflection erzeugen
        PdfPTable table = new PdfPTable(fields.length);
        table.setWidthPercentage(100); // füllt die Seite besser


        // Spaltenüberschriften
        for (Field field : fields) {
            field.setAccessible(true);

            // 🪄 Lesbaren Titel erzeugen
            String label = field.getName().replaceAll("([a-z])([A-Z])", "$1 $2"); // z.B. "taskTitle" → "task Title"

            PdfPCell header = new PdfPCell(new Phrase(label));
            header.setBackgroundColor(Color.LIGHT_GRAY);
            table.addCell(header);
        }


        // Datenzeilen
        for (T item : items) {
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    Object value = field.get(item);
                    if (value instanceof LocalDate) {
                        value = ((LocalDate) value).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                    } else if (value instanceof LocalDateTime) {
                        value = ((LocalDateTime) value).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                    }
                    table.addCell(value != null ? value.toString() : "");
                } catch (IllegalAccessException e) {
                    table.addCell("Zugriffsfehler");
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
}
