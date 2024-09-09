package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public ItemRequestDto createRequest(Long userId, CreateItemRequestDto createItemRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user not found"));
        ItemRequest itemRequest = ItemRequest.builder()
                .created(LocalDateTime.now())
                .requestor(user)
                .description(createItemRequestDto.getDescription())
                .build();
        return toDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDto> getOwnRequests(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user not found"));
        return toDto(itemRequestRepository.findAllByRequestorId(userId));
    }

    @Override
    public List<ItemRequestDto> getAllRequests() {
        return toDto(itemRequestRepository.findAll());
    }

    @Override
    public ItemRequestDto getRequestById(Long requestId) {
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("item request not found"));
        List<ItemDto> items = itemRepository.findAllByRequestId(requestId).stream()
                .map(ItemMapper.INSTANCE::itemToItemDto)
                .toList();
        ItemRequestDto itemRequestDto = toDto(itemRequest);
        itemRequestDto.setItems(items);
        return itemRequestDto;
    }

    private List<ItemRequestDto> toDto(List<ItemRequest> itemRequests) {
        return itemRequests.stream()
                .map(ItemRequestMapper.INSTANCE::itemRequestToItemRequestDto)
                .toList();
    }

    private ItemRequestDto toDto(ItemRequest itemRequest) {
        return ItemRequestMapper.INSTANCE.itemRequestToItemRequestDto(itemRequest);
    }
}
