package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserConstructorAndGetters() {
        User user = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");

        assertEquals(1L, user.getId());
        assertEquals("Иван Иванов", user.getName());
        assertEquals("ivan.ivanov@example.com", user.getEmail());
    }

    @Test
    void testSetters() {
        User user = new User();
        user.setId(2L);
        user.setName("Иван Иванов");
        user.setEmail("ivan.ivanov@example.com");

        assertEquals(2L, user.getId());
        assertEquals("Иван Иванов", user.getName());
        assertEquals("ivan.ivanov@example.com", user.getEmail());
    }

    @Test
    void testEquals() {
        User user1 = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");
        User user2 = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");
        User user3 = new User(2L, "Петр Петров", "petr.petrov@example.com");

        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
    }

    @Test
    void testHashCode() {
        User user1 = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");
        User user2 = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");

        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void testToString() {
        User user = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");
        String expected = "User(id=1, name=Иван Иванов, email=ivan.ivanov@example.com)";

        assertEquals(expected, user.toString());
    }

    @Test
    void testEqualsWithNull() {
        User user = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");
        assertNotEquals(user, null);
    }

    @Test
    void testEqualsWithDifferentClass() {
        User user = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");
        // Проверка, что объект User не равен объекту другого класса
        assertNotEquals(user, "some string");
    }

    @Test
    void testEqualsWithIdenticalData() {
        User user1 = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");
        User user2 = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");

        assertEquals(user1, user2);
    }
}
