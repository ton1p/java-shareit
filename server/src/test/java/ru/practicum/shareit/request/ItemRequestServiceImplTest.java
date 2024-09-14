package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ItemRequestServiceImplTest {
    private final ItemRequestService itemRequestService;
    private final UserService userService;

    @Test
    void createRequestWithUserNotFound() {
        assertThrows(NotFoundException.class, () -> itemRequestService.createRequest(1L, new CreateItemRequestDto("test")));
    }

    @Test
    void createRequest() {
        UserDto userDto = userService.create(new CreateUserDto("test", "test@test.com"));
        ItemRequestDto requestDto = itemRequestService.createRequest(userDto.getId(), new CreateItemRequestDto("test"));
        assertNotNull(requestDto);
    }

    @Test
    void getOwnRequestsWithUserNotFound() {
        assertThrows(NotFoundException.class, () -> itemRequestService.createRequest(1L, new CreateItemRequestDto("test")));
    }

    @Test
    void getOwnRequests() {
        UserDto userDto = userService.create(new CreateUserDto("test", "test@test.com"));
        ItemRequestDto requestDto = itemRequestService.createRequest(userDto.getId(), new CreateItemRequestDto("test"));
        assertNotNull(requestDto);
        assertEquals(List.of(requestDto), itemRequestService.getOwnRequests(userDto.getId()));
    }

    @Test
    void getAllRequests() {
        UserDto userDto = userService.create(new CreateUserDto("test", "test@test.com"));
        ItemRequestDto requestDto = itemRequestService.createRequest(userDto.getId(), new CreateItemRequestDto("test"));
        assertNotNull(requestDto);
        assertEquals(List.of(requestDto), itemRequestService.getAllRequests());
    }

    @Test
    void getRequestByIdWithItemNotFound() {
        assertThrows(NotFoundException.class, () -> itemRequestService.getRequestById(1L));
    }

    @Test
    void getRequestById() {
        UserDto userDto = userService.create(new CreateUserDto("test", "test@test.com"));
        ItemRequestDto requestDto = itemRequestService.createRequest(userDto.getId(), new CreateItemRequestDto("test"));
        assertNotNull(requestDto);
        assertEquals(requestDto, itemRequestService.getRequestById(requestDto.getId()));
    }
}
