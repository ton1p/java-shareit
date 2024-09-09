package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ItemRequestDto {
    private Long id;

    private String description;

    private LocalDateTime created;

    private List<ItemDto> items;

    public ItemRequestDto(Long id, String description, LocalDateTime created, List<ItemDto> items) {
        this.id = id;
        this.description = description;
        this.created = created;
        this.items = items == null ? new ArrayList<>() : items;
    }
}
