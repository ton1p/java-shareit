package ru.practicum.shareit.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class UserServiceImplTest {
    private final UserService userService;
    private final EntityManager em;

    @Test
    void findById() {
        CreateUserDto createUserDto = new CreateUserDto("test", "test@test.com");
        UserDto userDto = userService.create(createUserDto);

        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.id = :id", User.class);
        query.setParameter("id", userDto.getId());
        User user = query.getSingleResult();

        assertNotNull(user);
        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getName(), user.getName());
        assertEquals(userDto.getEmail(), user.getEmail());
    }

    @Test
    void findByIdNotFound() {
        assertThrows(NotFoundException.class, () -> userService.findById(1L));
    }

    @Test
    void findAll() {
        CreateUserDto createUserDto = new CreateUserDto("test", "test@test.com");
        userService.create(createUserDto);
        userService.create(createUserDto.toBuilder().name("test1").email("test1@test.com").build());

        List<UserDto> list = userService.findAll();
        assertEquals(2, list.size());
    }

    @Test
    void create() {
        CreateUserDto createUserDto = new CreateUserDto("test", "test@test.com");
        UserDto userDto = userService.create(createUserDto);
        assertNotNull(userDto);
    }

    @Test
    void createWithDuplicateEmail() {
        CreateUserDto createUserDto = new CreateUserDto("test", "test@test.com");
        UserDto userDto = userService.create(createUserDto);
        assertNotNull(userDto);
        assertThrows(ConflictException.class, () -> userService.create(createUserDto));
    }

    @Test
    void update() {
        CreateUserDto createUserDto = new CreateUserDto("test", "test@test.com");
        UserDto userDto = userService.create(createUserDto);
        assertNotNull(userDto);
        userDto.setName("test2");
        userDto.setEmail("test2@test.com");
        userService.update(userDto.getId(), userDto);
        assertEquals("test2", userDto.getName());
        assertEquals("test2@test.com", userDto.getEmail());
    }

    @Test
    void updateWithDuplicateEmail() {
        CreateUserDto createUserDto = new CreateUserDto("test", "test@test.com");
        UserDto userDto = userService.create(createUserDto);
        assertNotNull(userDto);
        assertThrows(ConflictException.class, () -> userService.update(userDto.getId(), userDto));
    }

    @Test
    void updateNotFound() {
        UserDto userDto = new UserDto(1L, "test", "test@test.com");
        assertThrows(NotFoundException.class, () -> userService.update(userDto.getId(), userDto));
    }

    @Test
    void delete() {
        CreateUserDto createUserDto = new CreateUserDto("test", "test@test.com");
        UserDto userDto = userService.create(createUserDto);
        assertNotNull(userDto);
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
        List<User> users = query.getResultList();
        assertEquals(1, users.size());
        userService.delete(userDto.getId());
        users = query.getResultList();
        assertEquals(0, users.size());
    }
}
