package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private static final String NOT_FOUND_MESSAGE = "User not found";

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream().map(UserMapper.INSTANCE::userToUserDto).toList();
    }

    @Override
    public UserDto findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundException(NOT_FOUND_MESSAGE);
        }
        return UserMapper.INSTANCE.userToUserDto(user.get());
    }

    @Override
    public UserDto create(CreateUserDto createUserDto) {
        if (userRepository.findByEmail(createUserDto.getEmail()).isPresent()) {
            throw new ConflictException("User with this email already exists");
        }
        User user = userRepository.save(UserMapper.INSTANCE.createUserDtoToUser(createUserDto));
        return UserMapper.INSTANCE.userToUserDto(user);
    }

    @Override
    public UserDto update(Long id, UserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new ConflictException("User with this email already exists");
        }
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new NotFoundException(NOT_FOUND_MESSAGE);
        }
        if (userDto.getName() != null && !userDto.getName().trim().isEmpty()) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null && !userDto.getEmail().trim().isEmpty()) {
            user.setEmail(userDto.getEmail());
        }
        return UserMapper.INSTANCE.userToUserDto(userRepository.save(user));
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
