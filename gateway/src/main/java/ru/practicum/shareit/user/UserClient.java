package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build());
    }

    public ResponseEntity<List<UserDto>> getAll() {
        return get("");
    }

    public ResponseEntity<UserDto> getById(final Long userId) {
        return get("/" + userId, userId);
    }

    public ResponseEntity<UserDto> create(final CreateUserDto createUserDto) {
        return post("", createUserDto);
    }

    public ResponseEntity<UserDto> update(final Long userId, final UserDto userDto) {
        return patch("/" + userId, userId, userDto);
    }

    public void delete(final Long userId) {
        delete("/" + userId, userId);
    }
}
