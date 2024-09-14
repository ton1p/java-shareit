package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(BookingController.class)
class BookingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    private final ItemDto itemDto = new ItemDto(1L, 1L, "test", "test", true);

    private final UserDto userDto = new UserDto(1L, "test", "test@test.com");

    private final BookingDto bookingDto = BookingDto.builder()
            .id(1L)
            .end(LocalDateTime.now())
            .start(LocalDateTime.now())
            .booker(userDto)
            .item(itemDto)
            .status(BookingStatus.WAITING)
            .build();

    @Test
    void addBooking() throws Exception {
        Mockito.when(bookingService.create(Mockito.anyLong(), Mockito.any())).thenReturn(bookingDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/bookings")
                                .header("X-Sharer-User-Id", 1)
                                .content(objectMapper.writeValueAsString(bookingDto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.id").value(bookingDto.getId()))
                .andExpect(jsonPath("$.booker").value(bookingDto.getBooker()))
                .andExpect(jsonPath("$.item").value(bookingDto.getItem()))
                .andExpect(jsonPath("$.status").value(bookingDto.getStatus().toString()));
    }

    @Test
    void updateBooking() throws Exception {
        Mockito.when(bookingService.update(Mockito.anyLong(), Mockito.any(), Mockito.anyBoolean())).thenReturn(bookingDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/bookings/{id}?approved={approved}", bookingDto.getId(), true)
                                .header("X-Sharer-User-Id", 1)
                )
                .andExpect(jsonPath("$.id").value(bookingDto.getId()))
                .andExpect(jsonPath("$.booker").value(bookingDto.getBooker()))
                .andExpect(jsonPath("$.item").value(bookingDto.getItem()))
                .andExpect(jsonPath("$.status").value(bookingDto.getStatus().toString()));
    }

    @Test
    void getBooking() throws Exception {
        Mockito.when(bookingService.getById(Mockito.anyLong(), Mockito.anyLong())).thenReturn(bookingDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/bookings/{id}", 1)
                                .header("X-Sharer-User-Id", 1)
                )
                .andExpect(jsonPath("$.id").value(bookingDto.getId()))
                .andExpect(jsonPath("$.booker").value(bookingDto.getBooker()))
                .andExpect(jsonPath("$.item").value(bookingDto.getItem()))
                .andExpect(jsonPath("$.status").value(bookingDto.getStatus().toString()));
    }

    @Test
    void getBookings() throws Exception {
        Mockito.when(bookingService.getAllByBookerIdAndState(Mockito.anyLong(), Mockito.any())).thenReturn(List.of(bookingDto));

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/bookings?state={state}", "ALL")
                                .header("X-Sharer-User-Id", 1)
                )
                .andExpect(jsonPath("$[0].id").value(bookingDto.getId()))
                .andExpect(jsonPath("$[0].booker").value(bookingDto.getBooker()))
                .andExpect(jsonPath("$[0].item").value(bookingDto.getItem()))
                .andExpect(jsonPath("$[0].status").value(bookingDto.getStatus().toString()));
    }

    @Test
    void getOwnerBookings() throws Exception {
        Mockito.when(bookingService.getAllByOwnerIdAndState(Mockito.anyLong(), Mockito.any())).thenReturn(List.of(bookingDto));

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/bookings/owner")
                                .header("X-Sharer-User-Id", 1)
                )
                .andExpect(jsonPath("$[0].id").value(bookingDto.getId()))
                .andExpect(jsonPath("$[0].booker").value(bookingDto.getBooker()))
                .andExpect(jsonPath("$[0].item").value(bookingDto.getItem()))
                .andExpect(jsonPath("$[0].status").value(bookingDto.getStatus().toString()));
    }
}
