package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public List<ItemDto> getItems(Long userId) {
        return itemRepository.getAll(userId).stream().map(ItemMapper.INSTANCE::itemToItemDto).toList();
    }

    @Override
    public List<ItemDto> search(String query) {
        return itemRepository.search(query).stream().map(ItemMapper.INSTANCE::itemToItemDto).toList();
    }

    @Override
    public ItemDto getItem(Long id) {
        Optional<Item> item = itemRepository.getById(id);
        return item.map(ItemMapper.INSTANCE::itemToItemDto).orElseThrow(() -> new NotFoundException("Item not found"));
    }

    @Override
    public ItemDto createItem(Long userId, CreateItemDto createItemDto) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        Item item = itemRepository.create(userId, ItemMapper.INSTANCE.createItemDtoToItem(createItemDto));
        return ItemMapper.INSTANCE.itemToItemDto(item);
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        Item item = itemRepository.getById(itemId).orElse(null);
        if (item == null) {
            throw new NotFoundException("Item not found");
        }
        if (item.getName() != null && !item.getName().trim().isEmpty()) {
            item.setName(itemDto.getName());
        }
        if (item.getDescription() != null && !item.getDescription().trim().isEmpty()) {
            item.setDescription(itemDto.getDescription());
        }
        if (item.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return ItemMapper.INSTANCE.itemToItemDto(itemRepository.update(itemId, item));
    }
}
