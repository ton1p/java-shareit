package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

@Service
public class ItemRequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build());
    }

    public ResponseEntity<ItemRequestDto> createRequest(
            final Long userId,
            final CreateItemRequestDto createItemRequestDto
    ) {
        return post("", userId, createItemRequestDto);
    }

    public ResponseEntity<List<ItemRequestDto>> getOwnRequests(
            final Long userId
    ) {
        return get("", userId);
    }

    public ResponseEntity<List<ItemRequestDto>> getAllRequests() {
        return get("/all");
    }

    public ResponseEntity<ItemRequestDto> getRequestById(@PathVariable("requestId") final Long requestId) {
        return get("/" + requestId);
    }
}
