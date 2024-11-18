package com.em.taskmanager.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private String reasonPhrase;
    private ErrorStatus errorCode;
    private String message;
    private String address;
    private int status;
    private long created;
}
