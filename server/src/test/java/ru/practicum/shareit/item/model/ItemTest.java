package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    void testItemConstructorAndGetters() {
        User owner = new User(1L, "Owner", "owner@example.com");
        ItemRequest request = new ItemRequest();
        Item item = new Item(1L, "Item Name", "Item Description", true, owner, request);

        assertEquals(1L, item.getId());
        assertEquals("Item Name", item.getName());
        assertEquals("Item Description", item.getDescription());
        assertTrue(item.getAvailable());
        assertEquals(owner, item.getOwner());
        assertEquals(request, item.getRequest());
    }

    @Test
    void testSetters() {
        User owner = new User(1L, "Owner", "owner@example.com");
        ItemRequest request = new ItemRequest();
        Item item = new Item();

        item.setId(2L);
        item.setName("New Item Name");
        item.setDescription("New Item Description");
        item.setAvailable(false);
        item.setOwner(owner);
        item.setRequest(request);

        assertEquals(2L, item.getId());
        assertEquals("New Item Name", item.getName());
        assertEquals("New Item Description", item.getDescription());
        assertFalse(item.getAvailable());
        assertEquals(owner, item.getOwner());
        assertEquals(request, item.getRequest());
    }

    @Test
    void testEquals() {
        User owner = new User(1L, "Owner", "owner@example.com");
        Item item1 = new Item(1L, "Item Name", "Item Description", true, owner, null);
        Item item2 = new Item(1L, "Item Name", "Item Description", true, owner, null);
        Item item3 = new Item(2L, "Another Item", "Another Description", true, owner, null);

        assertEquals(item1, item2);
        assertNotEquals(item1, item3);
    }

    @Test
    void testHashCode() {
        User owner = new User(1L, "Owner", "owner@example.com");
        Item item1 = new Item(1L, "Item Name", "Item Description", true, owner, null);
        Item item2 = new Item(1L, "Item Name", "Item Description", true, owner, null);

        assertEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    void testToString() {
        User owner = new User(1L, "Owner", "owner@example.com");
        Item item = new Item(1L, "Item Name", "Item Description", true, owner, null);
        String expected = "Item(id=1, name=Item Name, description=Item Description, available=true)";

        assertTrue(item.toString().contains(expected));
    }

    @Test
    void testEqualsWithNull() {
        User owner = new User(1L, "Owner", "owner@example.com");
        Item item = new Item(1L, "Item Name", "Item Description", true, owner, null);

        assertNotEquals(item, null);
    }

    @Test
    void testEqualsWithDifferentClass() {
        User owner = new User(1L, "Owner", "owner@example.com");
        Item item = new Item(1L, "Item Name", "Item Description", true, owner, null);

        assertNotEquals(item, new Object());
    }
}
