package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ItemServiceImplTest {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;

    @Test
    void getItems() {
        UserDto userDto = userService.create(new CreateUserDto("test", "test@test.com"));
        CreateItemDto createItemDto = new CreateItemDto("test", "test", true, 1L);
        ItemDto itemDto = itemService.createItem(userDto.getId(), createItemDto);
        assertThat(itemService.getItems(userDto.getId()), equalTo(List.of(itemDto)));
    }

    @Test
    void getItemUserNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> itemService.getItem(1L, 1L));
    }

    @Test
    void getItemNotFound() {
        UserDto userDto = userService.create(new CreateUserDto("test", "test@test.com"));
        Assertions.assertThrows(NotFoundException.class, () -> itemService.getItem(userDto.getId(), 1L));
    }

    @Test
    void getItem() {
        UserDto userDto = userService.create(new CreateUserDto("test", "test@test.com"));
        CreateItemDto createItemDto = new CreateItemDto("test", "test", true, 1L);
        ItemDto itemDto = itemService.createItem(userDto.getId(), createItemDto);
        ItemOwnerDto item = itemService.getItem(userDto.getId(), itemDto.getId());
        assertThat(item.getId(), equalTo(itemDto.getId()));
    }

    @Test
    void createItemWithUserNotFound() {
        CreateItemDto createItemDto = new CreateItemDto("test", "test", true, 1L);
        Assertions.assertThrows(NotFoundException.class, () -> itemService.createItem(1L, createItemDto));
    }

    @Test
    void createItem() {
        UserDto userDto = userService.create(new CreateUserDto("test", "test@test.com"));
        CreateItemDto createItemDto = new CreateItemDto("test", "test", true, 1L);
        ItemDto itemDto = itemService.createItem(userDto.getId(), createItemDto);
        assertThat(itemDto.getName(), equalTo(createItemDto.getName()));
        assertThat(itemDto.getDescription(), equalTo(createItemDto.getDescription()));
    }

    @Test
    void updateItemUserNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> {
            ItemDto itemDto = new ItemDto(1L, 1L, "test", "test", true);
            itemService.updateItem(1L, 1L, itemDto);
        });
    }

    @Test
    void updateItemItemNotFound() {
        UserDto userDto = userService.create(new CreateUserDto("test", "test@test.com"));
        ItemDto itemDto = new ItemDto(1L, userDto.getId(), "test", "test", true);
        Assertions.assertThrows(NotFoundException.class, () -> itemService.updateItem(userDto.getId(), 1L, itemDto));
    }

    @Test
    void updateItemSuccess() {
        UserDto userDto = userService.create(new CreateUserDto("test", "test@test.com"));
        ItemDto itemDto = itemService.createItem(userDto.getId(), new CreateItemDto("test", "test", true, 1L));
        itemDto.setName("test1");
        itemDto.setDescription("test1");
        itemDto.setAvailable(false);
        ItemDto updatedItem = itemService.updateItem(userDto.getId(), itemDto.getId(), itemDto);
        assertThat(updatedItem.getName(), equalTo("test1"));
        assertThat(updatedItem.getDescription(), equalTo("test1"));
        assertThat(updatedItem.getAvailable(), equalTo(false));
    }

    @Test
    void addComment() {
        CreateUserDto createUserDto = new CreateUserDto("test", "test@test.com");
        UserDto userDto = userService.create(createUserDto);

        CreateItemDto createItemDto = new CreateItemDto("test", "test", true, 1L);
        ItemDto itemDto = itemService.createItem(userDto.getId(), createItemDto);

        CreateBookingDto createBookingDto = new CreateBookingDto(itemDto.getId(), "2024-09-11T15:44:10", "2024-09-11T15:44:11");
        bookingService.create(userDto.getId(), createBookingDto);

        CommentDto commentDto = itemService.addComment(userDto.getId(), itemDto.getId(), new CreateCommentDto("test"));
        assertThat(commentDto.getText(), equalTo("test"));
        assertThat(commentDto.getAuthorName(), equalTo("test"));
    }

    @Test
    void addCommentBeforeBookingEnd() {
        CreateUserDto createUserDto = new CreateUserDto("test", "test@test.com");
        UserDto userDto = userService.create(createUserDto);

        CreateItemDto createItemDto = new CreateItemDto("test", "test", true, 1L);
        ItemDto itemDto = itemService.createItem(userDto.getId(), createItemDto);

        CreateBookingDto createBookingDto = new CreateBookingDto(itemDto.getId(), "2024-09-11T15:44:10", "2024-09-12T15:44:11");
        bookingService.create(userDto.getId(), createBookingDto);

        Assertions.assertThrows(BadRequestException.class, () -> itemService.addComment(userDto.getId(), itemDto.getId(), new CreateCommentDto("test")));
    }

    @Test
    void addCommentWithNoBooking() {
        CreateUserDto createUserDto = new CreateUserDto("test", "test@test.com");
        UserDto userDto = userService.create(createUserDto);

        CreateItemDto createItemDto = new CreateItemDto("test", "test", true, 1L);
        ItemDto itemDto = itemService.createItem(userDto.getId(), createItemDto);

        Assertions.assertThrows(BadRequestException.class, () -> itemService.addComment(userDto.getId(), itemDto.getId(), new CreateCommentDto("test")));
    }

    @Test
    void searchItemByName() {
        CreateUserDto createUserDto = new CreateUserDto("test", "test@test.com");
        UserDto userDto = userService.create(createUserDto);

        CreateItemDto createItemDto = new CreateItemDto("test", "test", true, 1L);
        ItemDto itemDto = itemService.createItem(userDto.getId(), createItemDto);

        assertThat(itemService.search("test"), equalTo(List.of(itemDto)));
    }
}
