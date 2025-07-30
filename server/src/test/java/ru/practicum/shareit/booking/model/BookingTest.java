package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingTest {

    @Test
    void testBookingConstructorAndGetters() {
        User booker = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");
        Item item = new Item(1L, "Item Name", "Item Description", true, booker, null);
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        Booking booking = new Booking(1L, start, end, item, booker, BookingStatus.APPROVED);

        assertEquals(1L, booking.getId());
        assertEquals(start, booking.getStart());
        assertEquals(end, booking.getEnd());
        assertEquals(item, booking.getItem());
        assertEquals(booker, booking.getBooker());
        assertEquals(BookingStatus.APPROVED, booking.getStatus());
    }

    @Test
    void testSetters() {
        User booker = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");
        Item item = new Item(1L, "Item Name", "Item Description", true, booker, null);
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        Booking booking = new Booking();

        booking.setId(2L);
        booking.setStart(start);
        booking.setEnd(end);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.REJECTED);

        assertEquals(2L, booking.getId());
        assertEquals(start, booking.getStart());
        assertEquals(end, booking.getEnd());
        assertEquals(item, booking.getItem());
        assertEquals(booker, booking.getBooker());
        assertEquals(BookingStatus.REJECTED, booking.getStatus());
    }

    @Test
    void testEquals() {
        User booker = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");
        Item item = new Item(1L, "Item Name", "Item Description", true, booker, null);
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        Booking booking1 = new Booking(1L, start, end, item, booker, BookingStatus.APPROVED);
        Booking booking2 = new Booking(1L, start, end, item, booker, BookingStatus.APPROVED);
        Booking booking3 = new Booking(2L, start, end, item, booker, BookingStatus.REJECTED);

        assertEquals(booking1, booking2);
        assertNotEquals(booking1, booking3);
    }

    @Test
    void testHashCode() {
        User booker = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");
        Item item = new Item(1L, "Item Name", "Item Description", true, booker, null);
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        Booking booking1 = new Booking(1L, start, end, item, booker, BookingStatus.APPROVED);
        Booking booking2 = new Booking(1L, start, end, item, booker, BookingStatus.APPROVED);

        assertEquals(booking1.hashCode(), booking2.hashCode());
    }

    @Test
    void testToString() {
        User booker = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");
        Item item = new Item(1L, "Item Name", "Item Description", true, booker, null);
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        Booking booking = new Booking(1L, start, end, item, booker, BookingStatus.APPROVED);
        String expected = "Booking(id=1, start=" + start + ", end=" + end + ", status=APPROVED)";

        assertEquals(expected, booking.toString());
    }

    @Test
    void testEqualsWithDifferentId() {
        User booker = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");
        Item item = new Item(1L, "Item Name", "Item Description", true, booker, null);
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        Booking booking1 = new Booking(1L, start, end, item, booker, BookingStatus.APPROVED);
        Booking booking2 = new Booking(2L, start, end, item, booker, BookingStatus.APPROVED);

        assertNotEquals(booking1, booking2);
    }

    @Test
    void testEqualsWithDifferentStart() {
        User booker = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");
        Item item = new Item(1L, "Item Name", "Item Description", true, booker, null);
        LocalDateTime start1 = LocalDateTime.now();
        LocalDateTime start2 = start1.plusDays(1);
        LocalDateTime end = start1.plusDays(2);
        Booking booking1 = new Booking(1L, start1, end, item, booker, BookingStatus.APPROVED);
        Booking booking2 = new Booking(1L, start2, end, item, booker, BookingStatus.APPROVED);

        assertEquals(booking1, booking2);
    }

    @Test
    void testEqualsWithDifferentEnd() {
        User booker = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");
        Item item = new Item(1L, "Item Name", "Item Description", true, booker, null);
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end1 = start.plusDays(1);
        LocalDateTime end2 = start.plusDays(2);
        Booking booking1 = new Booking(1L, start, end1, item, booker, BookingStatus.APPROVED);
        Booking booking2 = new Booking(1L, start, end2, item, booker, BookingStatus.APPROVED);

        assertEquals(booking1, booking2);
    }

    @Test
    void testEqualsWithDifferentItem() {
        User booker = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");
        Item item1 = new Item(1L, "Item Name", "Item Description", true, booker, null);
        Item item2 = new Item(2L, "Another Item", "Another Description", true, booker, null);
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        Booking booking1 = new Booking(1L, start, end, item1, booker, BookingStatus.APPROVED);
        Booking booking2 = new Booking(1L, start, end, item2, booker, BookingStatus.APPROVED);

        assertEquals(booking1, booking2);
    }

    @Test
    void testEqualsWithDifferentBooker() {
        User booker1 = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");
        User booker2 = new User(2L, "Петр Петров", "petr.petrov@example.com");
        Item item = new Item(1L, "Item Name", "Item Description", true, booker1, null);
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        Booking booking1 = new Booking(1L, start, end, item, booker1, BookingStatus.APPROVED);
        Booking booking2 = new Booking(1L, start, end, item, booker2, BookingStatus.APPROVED);

        assertEquals(booking1, booking2);
    }

    @Test
    void testEqualsWithDifferentStatus() {
        User booker = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");
        Item item = new Item(1L, "Item Name", "Item Description", true, booker, null);
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        Booking booking1 = new Booking(1L, start, end, item, booker, BookingStatus.APPROVED);
        Booking booking2 = new Booking(1L, start, end, item, booker, BookingStatus.REJECTED);

        assertEquals(booking1, booking2);
    }

    @Test
    void testEqualsWithNull() {
        User booker = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");
        Item item = new Item(1L, "Item Name", "Item Description", true, booker, null);
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        Booking booking = new Booking(1L, start, end, item, booker, BookingStatus.APPROVED);

        assertNotEquals(booking, null);
    }

    @Test
    void testEqualsWithDifferentClass() {
        User booker = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");
        Item item = new Item(1L, "Item Name", "Item Description", true, booker, null);
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        Booking booking = new Booking(1L, start, end, item, booker, BookingStatus.APPROVED);

        assertNotEquals(booking, new Object());
    }
}
