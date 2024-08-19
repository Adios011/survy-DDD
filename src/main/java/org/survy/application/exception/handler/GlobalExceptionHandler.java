package org.survy.application.exception.handler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.survy.domain.core.exception.*;

import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {



    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DomainException.class)
    public ErrorDto handleException(DomainException domainException) {
        log.error(domainException.getMessage(), domainException);
        return ErrorDto.builder()
                .statusCode(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(domainException.getMessage())
                .timestamp(new Date())
                .build();
    }


    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorDto handleNotFoundExceptions(NotFoundException exception) {
        log.error(exception.getMessage(), exception);
        return ErrorDto.builder()
                .statusCode(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(exception.getMessage())
                .timestamp(new Date())
                .build();
    }



    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorDto handleException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return ErrorDto.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("unexpected error!")
                .timestamp(new Date())
                .build();
    }


    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ErrorDto handleException(ValidationException validationException) {
        ErrorDto errorDto ;
        if (validationException instanceof ConstraintViolationException constraintViolationException) {
            String violations = extractViolationsFromException(constraintViolationException);
            errorDto = ErrorDto.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.getReasonPhrase())
                    .message(violations)
                    .timestamp(new Date())
                    .build();
        }else {
            String message = validationException.getMessage();
             errorDto = ErrorDto.builder()
                    .message(message)
                    .timestamp(new Date())
                    .statusCode(HttpStatus.BAD_REQUEST.getReasonPhrase())
                    .build();
        }

        return errorDto;
    }



    private String extractViolationsFromException(ConstraintViolationException constraintViolationException){
        return constraintViolationException.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("--"));
    }


    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidPropertyException.class)
    public ErrorDto handleException(InvalidPropertyException invalidPropertyException) {
        log.error(invalidPropertyException.getMessage(), invalidPropertyException);
        return ErrorDto.builder()
                .statusCode(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(invalidPropertyException.getMessage())
                .timestamp(new Date())
                .build();
    }



}
