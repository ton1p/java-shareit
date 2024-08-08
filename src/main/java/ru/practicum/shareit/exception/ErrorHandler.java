package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.shareit.error.ErrorResponse;

import java.util.ArrayList;
import java.util.List;

public class ErrorHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> errors = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(fieldName + " " + errorMessage);
        });
        return new ErrorResponse(e.getStatusCode().value(), errors);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    ErrorResponse handleNotFoundException(NotFoundException e) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), List.of(e.getMessage()));
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    ErrorResponse handleConflictException(ConflictException e) {
        return new ErrorResponse(HttpStatus.CONFLICT.value(), List.of(e.getMessage()));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalServerErrorException.class)
    ErrorResponse handleInternalServerErrorException(InternalServerErrorException e) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), List.of(e.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingRequestHeaderException.class)
    ErrorResponse handleMissingRequestHeaderException(MissingRequestHeaderException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), List.of(e.getMessage()));
    }
}
