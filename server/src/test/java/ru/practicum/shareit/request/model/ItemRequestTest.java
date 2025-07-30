package ru.practicum.shareit.request.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestTest {

    @Test
    void testItemRequestConstructorAndGetters() {
        User requestor = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");
        LocalDateTime created = LocalDateTime.now();
        ItemRequest itemRequest = new ItemRequest(1L, "Test Description", requestor, created);

        assertEquals(1L, itemRequest.getId());
        assertEquals("Test Description", itemRequest.getDescription());
        assertEquals(requestor, itemRequest.getRequestor());
        assertEquals(created, itemRequest.getCreated());
    }

    @Test
    void testSetters() {
        User requestor = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");
        LocalDateTime created = LocalDateTime.now();
        ItemRequest itemRequest = new ItemRequest();

        itemRequest.setId(2L);
        itemRequest.setDescription("New Description");
        itemRequest.setRequestor(requestor);
        itemRequest.setCreated(created);

        assertEquals(2L, itemRequest.getId());
        assertEquals("New Description", itemRequest.getDescription());
        assertEquals(requestor, itemRequest.getRequestor());
        assertEquals(created, itemRequest.getCreated());
    }

    @Test
    void testEquals() {
        User requestor = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");
        LocalDateTime created = LocalDateTime.now();
        ItemRequest itemRequest1 = new ItemRequest(1L, "Test Description", requestor, created);
        ItemRequest itemRequest2 = new ItemRequest(1L, "Test Description", requestor, created);
        ItemRequest itemRequest3 = new ItemRequest(2L, "Another Description", requestor, created);

        assertEquals(itemRequest1, itemRequest2);
        assertNotEquals(itemRequest1, itemRequest3);
        assertNotEquals(itemRequest1, null);
        assertNotEquals(itemRequest1, new Object());
    }

    @Test
    void testHashCode() {
        User requestor = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");
        LocalDateTime created = LocalDateTime.now();
        ItemRequest itemRequest1 = new ItemRequest(1L, "Test Description", requestor, created);
        ItemRequest itemRequest2 = new ItemRequest(1L, "Test Description", requestor, created);
        ItemRequest itemRequest3 = new ItemRequest(2L, "Another Description", requestor, created);

        assertEquals(itemRequest1.hashCode(), itemRequest2.hashCode());
        assertNotEquals(itemRequest1.hashCode(), itemRequest3.hashCode());
    }

    @Test
    void testToString() {
        User requestor = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");
        LocalDateTime created = LocalDateTime.now();
        ItemRequest itemRequest = new ItemRequest(1L, "Test Description", requestor, created);
        String expected = "ItemRequest(id=1, description=Test Description, created=" + created + ")";

        assertEquals(expected, itemRequest.toString());
    }

    @Test
    void testEqualsWithDifferentId() {
        User requestor = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");
        LocalDateTime created = LocalDateTime.now();
        ItemRequest itemRequest1 = new ItemRequest(1L, "Test Description", requestor, created);
        ItemRequest itemRequest2 = new ItemRequest(2L, "Test Description", requestor, created);

        assertNotEquals(itemRequest1, itemRequest2);
    }
}
