package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.dto.GetBookingState;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BookingServiceImplTest {
    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;
    private final BookingRepository bookingRepository;

    @Test
    void createWithUserNotFound() {
        CreateBookingDto createBookingDto = new CreateBookingDto(
                1L,
                "2024-09-11T15:44:10",
                "2024-09-11T15:44:11"
        );
        Assertions.assertThrows(NotFoundException.class, () -> bookingService.create(1L, createBookingDto));
    }

    @Test
    void createWithItemNotFound() {
        UserDto userDto = createUser();
        CreateBookingDto createBookingDto = new CreateBookingDto(1L, "2024-09-11T15:44:10", "2024-09-11T15:44:11");
        Assertions.assertThrows(NotFoundException.class, () -> bookingService.create(userDto.getId(), createBookingDto));
    }

    @Test
    void createWithItemNotAvailable() {
        UserDto userDto = createUser();
        ItemDto itemDto = createItem(userDto, false);
        CreateBookingDto createBookingDto = new CreateBookingDto(itemDto.getId(), "2024-09-11T15:44:10", "2024-09-11T15:44:11");
        Assertions.assertThrows(BadRequestException.class, () -> bookingService.create(userDto.getId(), createBookingDto));
    }

    @Test
    void create() {
        UserDto userDto = createUser();
        ItemDto itemDto = createItem(userDto, true);
        CreateBookingDto createBookingDto = new CreateBookingDto(itemDto.getId(), "2024-09-11T15:44:10", "2024-09-11T15:44:11");
        BookingDto bookingDto = bookingService.create(userDto.getId(), createBookingDto);
        Assertions.assertNotNull(bookingDto);
        Assertions.assertEquals(itemDto.getId(), bookingDto.getItem().getId());
        Assertions.assertEquals(BookingStatus.WAITING, bookingDto.getStatus());
        Assertions.assertEquals(userDto.getId(), bookingDto.getBooker().getId());
        Assertions.assertEquals(createBookingDto.getStart(), bookingDto.getStart().toString());
        Assertions.assertEquals(createBookingDto.getEnd(), bookingDto.getEnd().toString());
    }

    @Test
    void updateWithBookingNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> bookingService.update(1L, 1L, true));
    }

    @Test
    void updateWithWrongNotFound() {
        UserDto userDto = createUser();
        ItemDto itemDto = createItem(userDto, true);
        CreateBookingDto createBookingDto = new CreateBookingDto(itemDto.getId(), "2024-09-11T15:44:10", "2024-09-11T15:44:11");
        BookingDto bookingDto = bookingService.create(userDto.getId(), createBookingDto);

        Assertions.assertThrows(BadRequestException.class, () -> bookingService.update(3L, bookingDto.getId(), true));
    }

    @Test
    void update() {
        UserDto userDto = createUser();
        ItemDto itemDto = createItem(userDto, true);
        CreateBookingDto createBookingDto = new CreateBookingDto(itemDto.getId(), "2024-09-11T15:44:10", "2024-09-11T15:44:11");
        BookingDto bookingDto = bookingService.create(userDto.getId(), createBookingDto);

        BookingDto updated = bookingService.update(userDto.getId(), bookingDto.getId(), true);
        Assertions.assertNotNull(updated);
        Assertions.assertEquals(itemDto.getId(), updated.getItem().getId());
        Assertions.assertEquals(BookingStatus.APPROVED, updated.getStatus());
        Assertions.assertEquals(userDto.getId(), updated.getBooker().getId());
        Assertions.assertEquals(createBookingDto.getStart(), updated.getStart().toString());
        Assertions.assertEquals(createBookingDto.getEnd(), updated.getEnd().toString());

        updated = bookingService.update(userDto.getId(), bookingDto.getId(), false);
        Assertions.assertNotNull(updated);
        Assertions.assertEquals(BookingStatus.WAITING, updated.getStatus());
    }

    private ItemDto createItem(UserDto userDto, Boolean available) {
        return itemService.createItem(userDto.getId(), new CreateItemDto("test", "test", available, 1L));
    }

    private UserDto createUser() {
        return userService.create(new CreateUserDto("test", "test@test.com"));
    }

    @Test
    void getAllByBookerIdAndStateWithALLState() {
        UserDto userDto = createUser();
        ItemDto itemDto = createItem(userDto, true);
        CreateBookingDto createBookingDto = new CreateBookingDto(itemDto.getId(), "2024-09-11T15:44:10", "2024-09-11T15:44:11");
        bookingService.create(userDto.getId(), createBookingDto);
        List<BookingDto> list = bookingService.getAllByBookerIdAndState(userDto.getId(), GetBookingState.ALL);
        Assertions.assertNotNull(list);
        Assertions.assertEquals(1, list.size());
        Assertions.assertEquals(itemDto.getId(), list.getFirst().getItem().getId());
        Assertions.assertEquals(BookingStatus.WAITING, list.getFirst().getStatus());
        Assertions.assertEquals(userDto.getId(), list.getFirst().getBooker().getId());
        Assertions.assertEquals(createBookingDto.getStart(), list.getFirst().getStart().toString());
        Assertions.assertEquals(createBookingDto.getEnd(), list.getFirst().getEnd().toString());
    }

    @Test
    void getAllByBookerIdAndStateWithCURRENTState() {
        UserDto userDto = createUser();
        ItemDto itemDto = createItem(userDto, true);
        CreateBookingDto createBookingDto = new CreateBookingDto(itemDto.getId(), "2024-09-11T15:44:10", "2024-09-13T15:44:11");
        bookingService.create(userDto.getId(), createBookingDto);
        List<BookingDto> list = bookingService.getAllByBookerIdAndState(userDto.getId(), GetBookingState.CURRENT);
        Assertions.assertNotNull(list);
        Assertions.assertEquals(1, list.size());
        Assertions.assertEquals(itemDto.getId(), list.getFirst().getItem().getId());
        Assertions.assertEquals(BookingStatus.WAITING, list.getFirst().getStatus());
        Assertions.assertEquals(userDto.getId(), list.getFirst().getBooker().getId());
        Assertions.assertEquals(createBookingDto.getStart(), list.getFirst().getStart().toString());
        Assertions.assertEquals(createBookingDto.getEnd(), list.getFirst().getEnd().toString());
    }

    @Test
    void getAllByBookerIdAndStateWithPASTState() {
        UserDto userDto = createUser();
        ItemDto itemDto = createItem(userDto, true);
        CreateBookingDto createBookingDto = new CreateBookingDto(itemDto.getId(), "2024-09-11T15:44:10", "2024-09-11T15:44:11");
        bookingService.create(userDto.getId(), createBookingDto);
        List<BookingDto> list = bookingService.getAllByBookerIdAndState(userDto.getId(), GetBookingState.PAST);
        Assertions.assertNotNull(list);
        Assertions.assertEquals(1, list.size());
        Assertions.assertEquals(itemDto.getId(), list.getFirst().getItem().getId());
        Assertions.assertEquals(BookingStatus.WAITING, list.getFirst().getStatus());
        Assertions.assertEquals(userDto.getId(), list.getFirst().getBooker().getId());
        Assertions.assertEquals(createBookingDto.getStart(), list.getFirst().getStart().toString());
        Assertions.assertEquals(createBookingDto.getEnd(), list.getFirst().getEnd().toString());
    }

    @Test
    void getAllByBookerIdAndStateWithFUTUREState() {
        UserDto userDto = createUser();
        ItemDto itemDto = createItem(userDto, true);
        CreateBookingDto createBookingDto = new CreateBookingDto(itemDto.getId(), "2024-09-14T15:44:10", "2024-09-14T15:44:11");
        bookingService.create(userDto.getId(), createBookingDto);
        List<BookingDto> list = bookingService.getAllByBookerIdAndState(userDto.getId(), GetBookingState.FUTURE);
        Assertions.assertNotNull(list);
        Assertions.assertEquals(1, list.size());
        Assertions.assertEquals(itemDto.getId(), list.getFirst().getItem().getId());
        Assertions.assertEquals(BookingStatus.WAITING, list.getFirst().getStatus());
        Assertions.assertEquals(userDto.getId(), list.getFirst().getBooker().getId());
        Assertions.assertEquals(createBookingDto.getStart(), list.getFirst().getStart().toString());
        Assertions.assertEquals(createBookingDto.getEnd(), list.getFirst().getEnd().toString());
    }

    @Test
    void getAllByBookerIdAndStateWithWAITINGState() {
        UserDto userDto = createUser();
        ItemDto itemDto = createItem(userDto, true);
        CreateBookingDto createBookingDto = new CreateBookingDto(itemDto.getId(), "2024-09-15T15:44:10", "2024-09-16T15:44:11");
        bookingService.create(userDto.getId(), createBookingDto);
        List<BookingDto> list = bookingService.getAllByBookerIdAndState(userDto.getId(), GetBookingState.WAITING);
        Assertions.assertNotNull(list);
        Assertions.assertEquals(1, list.size());
        Assertions.assertEquals(itemDto.getId(), list.getFirst().getItem().getId());
        Assertions.assertEquals(BookingStatus.WAITING, list.getFirst().getStatus());
        Assertions.assertEquals(userDto.getId(), list.getFirst().getBooker().getId());
        Assertions.assertEquals(createBookingDto.getStart(), list.getFirst().getStart().toString());
        Assertions.assertEquals(createBookingDto.getEnd(), list.getFirst().getEnd().toString());
    }

    @Test
    void getAllByBookerIdAndStateWithREJECTEDState() {
        UserDto userDto = createUser();
        ItemDto itemDto = createItem(userDto, true);
        CreateBookingDto createBookingDto = new CreateBookingDto(itemDto.getId(), "2024-09-15T15:44:10", "2024-09-16T15:44:11");
        BookingDto bookingDto = bookingService.create(userDto.getId(), createBookingDto);
        bookingDto.setStatus(BookingStatus.REJECTED);
        bookingRepository.save(BookingMapper.INSTANCE.bookingDtoToBooking(bookingDto));
        List<BookingDto> list = bookingService.getAllByBookerIdAndState(userDto.getId(), GetBookingState.REJECTED);
        Assertions.assertNotNull(list);
        Assertions.assertEquals(1, list.size());
        Assertions.assertEquals(itemDto.getId(), list.getFirst().getItem().getId());
        Assertions.assertEquals(BookingStatus.REJECTED, list.getFirst().getStatus());
        Assertions.assertEquals(userDto.getId(), list.getFirst().getBooker().getId());
        Assertions.assertEquals(createBookingDto.getStart(), list.getFirst().getStart().toString());
        Assertions.assertEquals(createBookingDto.getEnd(), list.getFirst().getEnd().toString());
    }
}
