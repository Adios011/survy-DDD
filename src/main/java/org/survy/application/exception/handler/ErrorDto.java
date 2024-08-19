package org.survy.application.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
@Builder
public class ErrorDto {

    private String message;
    private String statusCode;
    private Date timestamp;


}
