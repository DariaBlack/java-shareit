package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BookingServiceImplTest {

    @Autowired
    private BookingServiceImpl bookingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User owner;
    private User booker;
    private Item item;
    private BookingRequestDto bookingRequestDto;

    @BeforeEach
    void setUp() {
        owner = new User(null, "Owner User", "owner@example.com");
        owner = userRepository.save(owner);

        booker = new User(null, "Booker User", "booker@example.com");
        booker = userRepository.save(booker);

        item = new Item(null, "Test Item", "Description", true, owner, null);
        item = itemRepository.save(item);

        bookingRequestDto = new BookingRequestDto(item.getId(), LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
    }

    @Test
    void createBooking() {
        BookingDto bookingDto = bookingService.createBooking(booker.getId(), bookingRequestDto);
        assertNotNull(bookingDto.getId());
        assertEquals(BookingStatus.WAITING, bookingDto.getStatus());
    }

    @Test
    void approveBooking() {
        BookingDto bookingDto = bookingService.createBooking(booker.getId(), bookingRequestDto);
        BookingDto approvedBooking = bookingService.approveBooking(owner.getId(), bookingDto.getId(), true);
        assertEquals(BookingStatus.APPROVED, approvedBooking.getStatus());
    }

    @Test
    void getBookingById() {
        BookingDto bookingDto = bookingService.createBooking(booker.getId(), bookingRequestDto);
        BookingDto foundBooking = bookingService.getBookingById(booker.getId(), bookingDto.getId());
        assertEquals(bookingDto.getId(), foundBooking.getId());
    }

    @Test
    void getUserBookings() {
        bookingService.createBooking(booker.getId(), bookingRequestDto);
        List<BookingDto> bookings = bookingService.getUserBookings(booker.getId(), "ALL");
        assertFalse(bookings.isEmpty());
    }

    @Test
    void getOwnerBookings() {
        bookingService.createBooking(booker.getId(), bookingRequestDto);
        List<BookingDto> bookings = bookingService.getOwnerBookings(owner.getId(), "ALL");
        assertFalse(bookings.isEmpty());
    }

    @Test
    void getBookingById_NotFound() {
        assertThrows(NotFoundException.class, () -> bookingService.getBookingById(booker.getId(), 999L));
    }
}