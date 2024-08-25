package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.dto.GetBookingState;
import ru.practicum.shareit.exception.ErrorHandler;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController extends ErrorHandler {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto addBooking(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody CreateBookingDto createBookingDto
    ) {
        return bookingService.create(userId, createBookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBooking(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId,
            @RequestParam(defaultValue = "false") Boolean approved
    ) {
        return bookingService.update(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId
    ) {
        return bookingService.getById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getBookings(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(value = "state", defaultValue = "ALL") GetBookingState state
    ) {
        return bookingService.getAllByBookerIdAndState(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getOwnerBookings(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(value = "state", defaultValue = "ALL") GetBookingState state
    ) {
        return bookingService.getAllByOwnerIdAndState(userId, state);
    }
}
