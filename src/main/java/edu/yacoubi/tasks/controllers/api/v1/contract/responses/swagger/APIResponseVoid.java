package edu.yacoubi.tasks.controllers.api.v1.contract.responses.swagger;

import edu.yacoubi.tasks.controllers.api.v1.contract.responses.APIResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Schema(description = "APIResponse ohne Payload (z. B. für DELETE)")
public class APIResponseVoid extends APIResponse<Void> {}
