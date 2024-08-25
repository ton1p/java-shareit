package ru.practicum.shareit.item;

import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;

import java.util.List;

public interface ItemService {
    List<ItemDto> getItems(Long userId);

    List<ItemDto> search(String query);

    ItemOwnerDto getItem(Long userId, Long id);

    ItemDto createItem(Long userId, CreateItemDto createItemDto);

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);

    CommentDto addComment(Long userId, Long itemId, CreateCommentDto createCommentDto);
}
