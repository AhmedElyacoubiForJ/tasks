package edu.yacoubi.tasks.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.yacoubi.tasks.exceptions.ApiErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class APIResponse<T> {
    private String status;
    private int statusCode;
    private String message;
    private List<ApiErrorResponse> errors;
    private T data;
}
