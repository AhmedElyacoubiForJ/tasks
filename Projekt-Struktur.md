```text
config/
    swagger/
        OpenApiConfig.java
        SwaggerGroupingConfig.java
        SwaggerTagsConfig.java
controllers/
    admin/
        InterceptorToggleController.java
        LoggingToggleController.java
    api/
        v1/
            contract/
            impl/
        v2/
            contract/
            impl/
    export/
        TaskListExportController.java
    view/
        TaskController.java (Controller gedacht für HTMX-Seiten)
        TaskListViewController.java (Controller gedacht für HTMX-Seiten)
domain/
    entities/
        enums/
            TaskListStatus.java
            TaskPriority.java
            TaskStatus.java
        Task.java
        TaskList.java
    exceptions/
        DomainRuleViolationException.java
        DomainValidationException.java
    TaskListUpdater.java
    TaskUpdater.java
exceptions/
    GlobalExceptionHandler.java
```

> API-Komponenten

```text
api/
    v1/
        contract/
            annotations/
                DefaultApiResponses.java
                DomainErrorsResponses.java
            dto/
                request/
                    task/
                        CreateTaskDto.java
                        FilterTaskDto.java
                        FullUpdateTaskDto.java
                        PatchTaskDto.java
                    tasklist/
                        ChangeTaskStatusRequest.java
                        CreateTaskListDto.java
                        PatchTaskListDto.java
                        TaskListFilterDto.java
                        TaskListPagesDto.java
                        UpdateTaskListDto.java
                    validation/ (Im Sinne vom SwaggerDocs, weil jakarta validation, wird nicht vernünftig gezeigt)
                        annotations/
                            ValidTaskDescription.java
                            ValidTaskDueDate.java
                            ValidTaskListDescription.java
                            ValidTaskListTitle.java
                            ValidTaskStatus.java
                            ValidTaskTitle.java
                            ValidTaskTitlePatch.java
                        validators/
                            ValidTaskDescriptionCustomizer.java
                            ValidTaskDueDateCustomizer.java
                            ValidTaskListDescriptionCustomizer.java
                            ValidTaskListTitleCustomizer.java
                            ValidTaskStatusValidator.java
                            ValidTaskTitleCustomizer.java
                            ValidTaskTitlePatchCustomizer.java
                response/
                    task/
                        TaskDetailDto.java
                        TaskSummaryDto.java
                    tasklist/
                        TaskListDto.java
                        TaskListWithTaskDetailDto.java
                        TaskListWithTaskSummaryDto.java
            responses/
                wrappers/
                    APIResponseListTaskListDto.java
                    APIResponseListTaskSummaryDto.java
                    APIResponseTaskListDto.java
                    APIResponseTaskSummaryDto.java
                    APIResponseVoid.java
                ApiErrorResponse.java
                APIResponse.java
                ResponseStatus.java  
            utils/
                ApiResponseHelper.java
            IApiPrefix
            ITaskListsCrudApi.java
            ITaskListsTasksCrudApi.java
            ITaskListsUseCaseApi.java      
        impl/
            TaskListsCrudController.java
            TaskListsTasksCrudController.java
            TaskListsUseCaseController.java
    v2/
        contract/
        impl/    
```

```text
domain/
    entities/
        enums/
            TaskListStatus.java
            TaskPriority.java
            TaskStatus.java
        Task.java
        TaskList.java
    exceptions/
        DomainRuleViolationException.java
        DomainValidationException.java
        GlobalExceptionHandler.java
    TaskListUpdater.java
    TaskUpdater.java
interceptor/
    ConsoleLoggingInterceptor.java
    JsonLoggingInterceptor.java
mappers/
    ITransformer.java
    TaskListTransformer.java
    TaskTransformer.java
    TransformerUtil.java
repositories/
    TaskListRepository.java
    TaskRepository.java
services/
    app/
        impl/
            TaskListServiceImpl.java
            TaskListsTaskOrchestratorImpl.java
            TaskServiceImpl.java     
        ITaskListService.java
        ITaskListsTaskOrchestrator.java
        ITaskService.java
    export/
        IPdfExportService.java
        PdfExportServiceImpl.java     
    init/
        DatabaseInitializerService.java (@Profile("local-dev"))
    ui/
        IProgressColorService.java
        ProgressColorServiceImpl.java  
TasksApplication.java
```


```text
Dann sortieren wir alles sauber in:

domain/model

domain/services

domain/events

domain/exceptions

application/usecases

infrastructure/persistence

Damit wird dein Projekt wirklich DDD‑rein.
```