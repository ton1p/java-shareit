package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.practicum.shareit.exception.ErrorHandler;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

@Controller
@RequestMapping(path = "/requests")
@AllArgsConstructor
@Validated
public class ItemRequestController extends ErrorHandler {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    ResponseEntity<ItemRequestDto> createRequest(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody @Valid final CreateItemRequestDto createItemRequestDto
    ) {
        return itemRequestClient.createRequest(userId, createItemRequestDto);
    }

    @GetMapping
    ResponseEntity<List<ItemRequestDto>> getOwnRequests(
            @RequestHeader("X-Sharer-User-Id") final Long userId
    ) {
        return itemRequestClient.getOwnRequests(userId);
    }

    @GetMapping("/all")
    ResponseEntity<List<ItemRequestDto>> getAllRequests() {
        return itemRequestClient.getAllRequests();
    }

    @GetMapping("/{requestId}")
    ResponseEntity<ItemRequestDto> getRequestById(@PathVariable("requestId") final Long requestId) {
        return itemRequestClient.getRequestById(requestId);
    }
}
