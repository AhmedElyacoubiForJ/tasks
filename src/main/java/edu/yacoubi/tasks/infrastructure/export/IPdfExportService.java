package edu.yacoubi.tasks.infrastructure.export;

import java.util.List;

public interface IPdfExportService<T> {
    byte[] exportAsPdf(List<T> items);                // Für REST, E-Mail usw.

    byte[] exportAsCsv(List<T> items);

    void exportPdfToFile(List<T> items, String targetPath);  // Direktes Speichern

    void exportCsvToFile(List<T> items, String targetPath);
}

