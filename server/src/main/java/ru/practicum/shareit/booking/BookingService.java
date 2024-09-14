package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.dto.GetBookingState;

import java.util.List;

public interface BookingService {
    List<BookingDto> getAllByBookerIdAndState(Long userId, GetBookingState state);

    List<BookingDto> getAllByOwnerIdAndState(Long userId, GetBookingState state);

    BookingDto getById(Long userId, Long bookingId);

    BookingDto create(Long userId, CreateBookingDto createBookingDto);

    BookingDto update(Long userId, Long bookingId, Boolean approved);
}
