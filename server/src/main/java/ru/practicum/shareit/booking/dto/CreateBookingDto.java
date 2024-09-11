package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingDto {
    @NotNull
    private Long itemId;

    @NotNull
    @NotEmpty
    @NotBlank
    private String start;

    @NotNull
    @NotEmpty
    @NotBlank
    private String end;
}
