package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.exception.ErrorHandler;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController extends ErrorHandler {
    private final ItemRequestService itemRequestService;

    @PostMapping
    ItemRequestDto createRequest(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody @Valid final CreateItemRequestDto createItemRequestDto
    ) {
        return itemRequestService.createRequest(userId, createItemRequestDto);
    }

    @GetMapping
    List<ItemRequestDto> getOwnRequests(
            @RequestHeader("X-Sharer-User-Id") final Long userId
    ) {
        return itemRequestService.getOwnRequests(userId);
    }

    @GetMapping("/all")
    List<ItemRequestDto> getAllRequests() {
        return itemRequestService.getAllRequests();
    }

    @GetMapping("/{requestId}")
    ItemRequestDto getRequestById(@PathVariable("requestId") final Long requestId) {
        return itemRequestService.getRequestById(requestId);
    }
}
