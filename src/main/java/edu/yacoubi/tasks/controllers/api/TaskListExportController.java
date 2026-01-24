package edu.yacoubi.tasks.controllers.api;

import edu.yacoubi.tasks.domain.dto.response.tasklist.TaskListDto;
import edu.yacoubi.tasks.mappers.TaskListMapper;
import edu.yacoubi.tasks.services.app.ITaskListService;
import edu.yacoubi.tasks.services.export.IExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@RestController
@RequestMapping("/api/tasklists")
@Tag(name = "TaskList Export", description = "Exportiere Task-Listen als PDF")
@RequiredArgsConstructor
public class TaskListExportController {

    private final ITaskListService taskListService;
    private final IExportService<TaskListDto> pdfExportService;
    private final TaskListMapper taskListMapper;

    @Operation(
            summary = "📄 Exportiere alle Task-Listen als PDF",
            description = "Gibt ein PDF-Dokument mit allen Task-Listen zurück. Formatierte Tabelle mit Titeln, Beschreibungen und Fortschritt.",
            tags = {"TaskList Export"}
    )
    @GetMapping(value = "/export/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> exportAsPdf() {
        List<TaskListDto> taskLists = taskListService.getAllTaskLists()
                .stream()
                .map(taskListMapper::toTaskListDto)
                .toList();
        byte[] pdf = pdfExportService.exportAsPdf(taskLists);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=tasklists.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}

