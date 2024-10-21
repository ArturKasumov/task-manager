package com.arturk.taskmanagerms.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Response for exception")
public class RestException {

    @Schema(description = "Code of the exception")
    private String code;

    @Schema(description = "Message of the exception")
    private String message;

    @Schema(description = "Details of the exception")
    private String details;

    public RestException(String code, String description, String details) {
        this.code = code;
        this.message = description;
        this.details = details;
    }
}
