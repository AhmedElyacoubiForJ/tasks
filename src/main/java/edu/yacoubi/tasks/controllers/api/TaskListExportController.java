package edu.yacoubi.tasks.controllers.api;

import edu.yacoubi.tasks.domain.dto.response.tasklist.TaskListDto;
import edu.yacoubi.tasks.mappers.TaskListMapper;
import edu.yacoubi.tasks.services.app.ITaskListService;
import edu.yacoubi.tasks.services.export.IExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tasklists")
@RequiredArgsConstructor
public class TaskListExportController {

    private final ITaskListService taskListService;
    private final IExportService<TaskListDto> pdfExportService;
    private final TaskListMapper taskListMapper;

    @GetMapping(value = "/export/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> exportAsPdf() {
        List<TaskListDto> taskLists = taskListService.listTaskLists()
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

