package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    List<Item> getAll(Long userId);

    List<Item> search(String query);

    Optional<Item> getById(Long id);

    Item create(Long userId, CreateItemDto createItemDto);

    Item update(Long itemId, ItemDto itemDto);
}
