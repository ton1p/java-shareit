package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> findAll();

    Optional<User> findById(final Long id);

    Optional<User> findByEmail(final String email);

    User create(final CreateUserDto createUserDto);

    User update(final Long id, final UserDto userDto);

    void delete(final Long id);
}
