package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateBookingDto {
    @NotNull
    Long itemId;

    @NotNull
    @NotEmpty
    @NotBlank
    String start;

    @NotNull
    @NotEmpty
    @NotBlank
    String end;
}
