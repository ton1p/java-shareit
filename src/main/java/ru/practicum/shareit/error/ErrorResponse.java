package ru.practicum.shareit.error;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private int statusCode;
    private List<String> errors;
}
