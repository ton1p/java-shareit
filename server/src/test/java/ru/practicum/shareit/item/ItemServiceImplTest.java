package ru.practicum.shareit.item;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
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
class ItemServiceImplTest {
    private final ItemService itemService;
    private final UserService userService;

    private final EntityManager em;

    @Test
    void getItem() {
        CreateUserDto createUserDto = new CreateUserDto("test", "test@test.com");
        UserDto userDto = userService.create(createUserDto);

        CreateItemDto createItemDto = new CreateItemDto("test", "test", true, 1L);

        ItemDto itemDto = itemService.createItem(userDto.getId(), createItemDto);

        TypedQuery<Item> query = em.createQuery("select i from Item i where i.id = :id", Item.class);
        query.setParameter("id", itemDto.getId());
        Item i = query.getSingleResult();

        assertThat(i.getId(), equalTo(itemDto.getId()));
        assertThat(i.getName(), equalTo(itemDto.getName()));
        assertThat(i.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(i.getAvailable(), equalTo(itemDto.getAvailable()));
        assertThat(i.getRequestId(), equalTo(1L));
    }
}
