package ru.practicum.shareit.request.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;


@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemRequestDto createItemRequest(@RequestHeader(USER_ID_HEADER) @Positive Long userId,
                                            @Valid @RequestBody ItemRequestCreateDto itemRequestCreateDto) {
        return itemRequestService.createItemRequest(userId, itemRequestCreateDto);
    }

    @GetMapping
    public List<ItemRequestDto> getItemRequestsByUserId(@RequestHeader(USER_ID_HEADER) @Positive Long userId) {
        return itemRequestService.getItemRequestsByUserId(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllItemRequests(@RequestHeader(USER_ID_HEADER) @Positive Long userId,
                                                   @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                   @RequestParam(defaultValue = "10") @Positive Integer size) {
        return itemRequestService.getAllItemRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequestById(@RequestHeader(USER_ID_HEADER) @Positive Long userId,
                                             @PathVariable @Positive Long requestId) {
        return itemRequestService.getItemRequestById(userId, requestId);
    }
}
