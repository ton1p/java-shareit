package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.dto.GetBookingState;
import ru.practicum.shareit.client.BaseClient;

import java.util.List;
import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<List<BookingDto>> getBookings(long userId, GetBookingState state) {
        return get("?state=" + state, userId);
    }

    public ResponseEntity<List<BookingDto>> getOwnerBookings(long userId, GetBookingState state) {
        return get("/owner?state=" + state, userId);
    }


    public ResponseEntity<BookingDto> addBooking(long userId, CreateBookingDto requestDto) {
        return post("", userId, requestDto);
    }

    public ResponseEntity<BookingDto> updateBooking(long userId, Long bookingId, Boolean approved) {
        Map<String, Object> parameters = Map.of("bookingId", bookingId, "approved", approved);
        return patch("/" + bookingId + "?approved=" + approved, userId, parameters);
    }

    public ResponseEntity<BookingDto> getBooking(long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }
}
