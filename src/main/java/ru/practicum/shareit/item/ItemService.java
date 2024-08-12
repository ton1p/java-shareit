package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    List<ItemDto> getItems(Long userId);

    List<ItemDto> search(String query);

    ItemDto getItem(Long id);

    ItemDto createItem(Long userId, CreateItemDto createItemDto);

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);
}
