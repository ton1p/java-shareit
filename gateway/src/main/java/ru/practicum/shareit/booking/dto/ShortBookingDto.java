package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
public class ShortBookingDto {
    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private UserDto booker;

    private BookingStatus status;
}
