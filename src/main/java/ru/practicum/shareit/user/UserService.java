package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> findAll();

    UserDto findById(Long id);
    
    UserDto create(CreateUserDto createUserDto);

    UserDto update(Long id, UserDto userDto);

    void delete(Long id);
}
