package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.exception.ErrorHandler;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
@Validated
public class UserController extends ErrorHandler {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        return userClient.getAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getById(@PathVariable final Long userId) {
        return userClient.getById(userId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody final CreateUserDto createUserDto) {
        return userClient.create(createUserDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@PathVariable final Long userId, @Valid @RequestBody final UserDto userDto) {
        return userClient.update(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable final Long userId) {
        userClient.delete(userId);
    }
}
