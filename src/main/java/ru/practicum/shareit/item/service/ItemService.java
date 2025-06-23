package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item createItem(Long userId, Item item);

    Item updateItem(Long userId, Long itemId, Item item);

    Item getItemById(Long itemId);

    List<Item> getItemsByUserId(Long userId);

    List<Item> searchItems(String query);

    void deleteItem(Long userId, Long itemId);
}
