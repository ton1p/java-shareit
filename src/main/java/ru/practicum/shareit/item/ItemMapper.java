package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.user.User;

@Mapper
public interface ItemMapper {
    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    @Mapping(source = "owner", target = "ownerId")
    ItemDto itemToItemDto(Item item);

    @Mapping(source = "owner", target = "ownerId")
    ItemOwnerDto itemToItemOwnerDto(Item item);

    Item createItemDtoToItem(CreateItemDto createItemDto);

    default Long map(User user) {
        return user.getId();
    }
}
