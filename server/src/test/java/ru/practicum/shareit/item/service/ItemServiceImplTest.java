package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemServiceImplTest {

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User owner;
    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        owner = new User(null, "Owner User", "owner@example.com");
        owner = userRepository.save(owner);

        itemDto = new ItemDto(null, "Test Item", "Description", true, null, null);
    }

    @Test
    void createItem() {
        ItemDto createdItem = itemService.createItem(owner.getId(), itemDto);
        assertNotNull(createdItem.getId());
        assertEquals(itemDto.getName(), createdItem.getName());
    }

    @Test
    void updateItem() {
        ItemDto createdItem = itemService.createItem(owner.getId(), itemDto);
        ItemDto updateDto = new ItemDto(null, "Updated Item", "Updated Description", false, null, null);
        ItemDto updatedItem = itemService.updateItem(owner.getId(), createdItem.getId(), updateDto);
        assertEquals(updateDto.getName(), updatedItem.getName());
        assertEquals(updateDto.getDescription(), updatedItem.getDescription());
        assertFalse(updatedItem.getAvailable());
    }

    @Test
    void getItemById() {
        ItemDto createdItem = itemService.createItem(owner.getId(), itemDto);
        ItemWithBookingDto foundItem = itemService.getItemById(owner.getId(), createdItem.getId());
        assertEquals(createdItem.getId(), foundItem.getId());
    }

    @Test
    void getItemsByUserId() {
        itemService.createItem(owner.getId(), itemDto);
        List<ItemWithBookingDto> items = itemService.getItemsByUserId(owner.getId());
        assertFalse(items.isEmpty());
    }

    @Test
    void searchItems() {
        itemService.createItem(owner.getId(), itemDto);
        List<ItemDto> items = itemService.searchItems("Test");
        assertFalse(items.isEmpty());
    }

    @Test
    void deleteItem() {
        ItemDto createdItem = itemService.createItem(owner.getId(), itemDto);
        itemService.deleteItem(owner.getId(), createdItem.getId());
        assertThrows(NotFoundException.class, () -> itemService.getItemById(owner.getId(), createdItem.getId()));
    }
}