package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);

    ItemWithBookingDto getItemById(Long userId, Long itemId);

    List<ItemWithBookingDto> getItemsByUserId(Long userId);

    List<ItemDto> searchItems(String text);

    void deleteItem(Long userId, Long itemId);

    CommentDto addComment(Long userId, Long itemId, CommentRequestDto commentRequestDto);
}