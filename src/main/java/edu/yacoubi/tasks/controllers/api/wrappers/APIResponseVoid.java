package edu.yacoubi.tasks.controllers.api.wrappers;

import edu.yacoubi.tasks.controllers.api.APIResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Schema(description = "APIResponse ohne Payload (z. B. für DELETE)")
public class APIResponseVoid extends APIResponse<Void> {}
