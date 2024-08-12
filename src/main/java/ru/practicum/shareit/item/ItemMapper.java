package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;

@Mapper
public interface ItemMapper {
    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    ItemDto itemToItemDto(Item item);

    Item createItemDtoToItem(CreateItemDto createItemDto);

    Item itemDtoToItem(ItemDto itemDto);
}
