package ru.practicum.shareit.item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    List<Item> getAll(Long userId);

    List<Item> search(String query);

    Optional<Item> getById(Long id);

    Item create(Long userId, Item item);

    Item update(Long itemId, Item item);
}
