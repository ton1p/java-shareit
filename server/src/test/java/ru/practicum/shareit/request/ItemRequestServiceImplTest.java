package ru.practicum.shareit.request;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ItemRequestServiceImplTest {
    private final ItemRequestService itemRequestService;
    private final UserService userService;
    private final EntityManager em;

    @Test
    void getRequestById() {
        UserDto userDto = userService.create(new CreateUserDto("test", "test@test.com"));
        ItemRequestDto itemRequestDto = itemRequestService.createRequest(userDto.getId(), new CreateItemRequestDto("test"));

        TypedQuery<ItemRequest> query = em.createQuery("select r from ItemRequest r where r.id = :id", ItemRequest.class);
        query.setParameter("id", itemRequestDto.getId());
        ItemRequest itemRequest = query.getSingleResult();

        assertNotNull(itemRequest);
        assertEquals(itemRequestDto.getId(), itemRequest.getId());
        assertEquals(itemRequestDto.getItems(), List.of());
        assertEquals(itemRequestDto.getDescription(), itemRequest.getDescription());
    }
}
