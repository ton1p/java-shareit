package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;
import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build());
    }

    public ResponseEntity<List<ItemDto>> getItems(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<ItemDto> getItem(
            Long userId,
            Long itemId
    ) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<List<ItemDto>> search(
            Long userId,
            String text
    ) {
        return get("/search?text=" + text, userId);
    }

    public ResponseEntity<ItemDto> createItem(
            Long userId,
            CreateItemDto createItemDto
    ) {
        return post("", userId, createItemDto);
    }

    public ResponseEntity<ItemDto> updateItem(
            Long userId,
            Long itemId,
            ItemDto itemDto
    ) {
        return patch("/" + itemId, userId, itemDto);
    }

    public ResponseEntity<CommentDto> addComment(
            Long userId,
            Long itemId,
            CreateCommentDto createCommentDto
    ) {
        Map<String, Object> params = Map.of("itemId", itemId);
        return post("/" + itemId + "/comment", userId, params, createCommentDto);
    }
}
