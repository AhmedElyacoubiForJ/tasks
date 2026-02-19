package edu.yacoubi.tasks.controllers.api.wrappers;

import edu.yacoubi.tasks.controllers.api.APIResponse;
import edu.yacoubi.tasks.domain.dto.response.task.TaskSummaryDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter @Setter
@Schema(description = "APIResponse mit TaskSummaryDto als Payload")
public class APIResponseTaskSummaryDto extends APIResponse<TaskSummaryDto> {}
