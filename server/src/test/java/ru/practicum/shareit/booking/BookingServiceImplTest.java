package ru.practicum.shareit.booking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BookingServiceImplTest {
    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;

    private final EntityManager entityManager;

    @Test
    void getById() {
        UserDto userDto = userService.create(new CreateUserDto("test", "test@test.com"));

        ItemDto itemDto = itemService.createItem(userDto.getId(), new CreateItemDto(
                "test",
                "test",
                true,
                1L
        ));

        BookingDto createdBookingDto = bookingService.create(userDto.getId(), new CreateBookingDto(
                itemDto.getId(),
                "2024-09-11T14:04:36",
                "2024-09-11T14:04:37"
        ));

        TypedQuery<Booking> query = entityManager.createQuery("select b from Booking b where b.id = :bookingId", Booking.class);
        query.setParameter("bookingId", createdBookingDto.getId());
        Booking booking = query.getSingleResult();

        assertThat(booking.getId(), equalTo(1L));
        assertThat(booking.getBooker().getId(), equalTo(userDto.getId()));
        assertThat(booking.getItem().getId(), equalTo(itemDto.getId()));
        assertThat(booking.getStart(), equalTo(createdBookingDto.getStart()));
        assertThat(booking.getEnd(), equalTo(createdBookingDto.getEnd()));
        assertThat(booking.getStatus(), equalTo(BookingStatus.WAITING));
    }
}
