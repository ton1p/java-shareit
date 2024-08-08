package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
public class InMemoryItemRepository implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private long id;

    @Override
    public List<Item> getAll(Long userId) {
        return new ArrayList<>(
                items.values()
                        .stream()
                        .filter(item -> Objects.equals(item.getOwnerId(), userId))
                        .toList()
        );
    }

    @Override
    public List<Item> search(String query) {
        if (items.values().isEmpty()) {
            return List.of();
        }
        return items.values().stream()
                .filter(item -> item.getAvailable() != null && item.getAvailable())
                .filter(item -> (item.getName() != null && item.getName().equalsIgnoreCase(query)) || (item.getDescription() != null && item.getDescription().equalsIgnoreCase(query)))
                .toList();
    }

    @Override
    public Optional<Item> getById(Long id) {
        if (items.containsKey(id)) {
            return Optional.of(items.get(id));
        }
        return Optional.empty();
    }

    @Override
    public Item create(Long userId, CreateItemDto createItemDto) {
        id++;
        Item item = ItemMapper.INSTANCE.createItemDtoToItem(createItemDto);
        item.setId(id);
        item.setOwnerId(userId);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Long itemId, ItemDto itemDto) {
        Item item = items.get(itemId);
        if (item.getName() != null && !item.getName().isEmpty() && !item.getName().isBlank()) {
            item.setName(itemDto.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isEmpty() && !item.getDescription().isBlank()) {
            item.setDescription(itemDto.getDescription());
        }
        if (item.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return item;
    }
}
