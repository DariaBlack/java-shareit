package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotAvailableException;
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

    @Test
    void createBooking_ItemNotAvailable() {
        item.setAvailable(false);
        itemRepository.save(item);

        assertThrows(NotAvailableException.class, () ->
                bookingService.createBooking(booker.getId(), bookingRequestDto));
    }

    @Test
    void createBooking_OwnerBookingOwnItem() {
        assertThrows(NotFoundException.class, () -> bookingService.createBooking(owner.getId(), bookingRequestDto));
    }

    @Test
    void createBooking_OverlappingBooking() {
        bookingService.createBooking(booker.getId(), bookingRequestDto);
        assertThrows(ConflictException.class, () -> bookingService.createBooking(booker.getId(), bookingRequestDto));
    }

    @Test
    void approveBooking_NotOwner() {
        BookingDto bookingDto = bookingService.createBooking(booker.getId(), bookingRequestDto);
        User tempUser = new User(null, "Another User", "another@example.com");
        final User anotherUser = userRepository.save(tempUser);

        assertThrows(AccessDeniedException.class, () -> bookingService.approveBooking(anotherUser.getId(), bookingDto.getId(), true));
    }

    @Test
    void approveBooking_AlreadyApproved() {
        BookingDto bookingDto = bookingService.createBooking(booker.getId(), bookingRequestDto);
        bookingService.approveBooking(owner.getId(), bookingDto.getId(), true);

        assertThrows(ConflictException.class, () -> bookingService.approveBooking(owner.getId(), bookingDto.getId(), true));
    }

    @Test
    void getBookingById_NotAuthorized() {
        BookingDto bookingDto = bookingService.createBooking(booker.getId(), bookingRequestDto);
        User tempUser = new User(null, "Another User", "another@example.com");
        final User anotherUser = userRepository.save(tempUser);

        assertThrows(NotFoundException.class, () -> bookingService.getBookingById(anotherUser.getId(), bookingDto.getId()));
    }

    @Test
    void getUserBookings_InvalidState() {
        assertThrows(IllegalArgumentException.class, () -> bookingService.getUserBookings(booker.getId(), "INVALID_STATE"));
    }

    @Test
    void getOwnerBookings_InvalidState() {
        assertThrows(IllegalArgumentException.class, () -> bookingService.getOwnerBookings(owner.getId(), "INVALID_STATE"));
    }

    @Test
    void getUserBookings_CurrentState() {
        bookingService.createBooking(booker.getId(), bookingRequestDto);
        List<BookingDto> bookings = bookingService.getUserBookings(booker.getId(), "CURRENT");
        assertTrue(bookings.isEmpty()); // Assuming no current bookings
    }

    @Test
    void getUserBookings_PastState() {
        bookingRequestDto = new BookingRequestDto(item.getId(), LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1));
        bookingService.createBooking(booker.getId(), bookingRequestDto);
        List<BookingDto> bookings = bookingService.getUserBookings(booker.getId(), "PAST");
        assertFalse(bookings.isEmpty());
    }

    @Test
    void getUserBookings_FutureState() {
        bookingService.createBooking(booker.getId(), bookingRequestDto);
        List<BookingDto> bookings = bookingService.getUserBookings(booker.getId(), "FUTURE");
        assertFalse(bookings.isEmpty());
    }

    @Test
    void getUserBookings_WaitingState() {
        bookingService.createBooking(booker.getId(), bookingRequestDto);
        List<BookingDto> bookings = bookingService.getUserBookings(booker.getId(), "WAITING");
        assertFalse(bookings.isEmpty());
    }

    @Test
    void getUserBookings_RejectedState() {
        BookingDto bookingDto = bookingService.createBooking(booker.getId(), bookingRequestDto);
        bookingService.approveBooking(owner.getId(), bookingDto.getId(), false);
        List<BookingDto> bookings = bookingService.getUserBookings(booker.getId(), "REJECTED");
        assertFalse(bookings.isEmpty());
    }

    @Test
    void getOwnerBookings_CurrentState() {
        bookingService.createBooking(booker.getId(), bookingRequestDto);
        List<BookingDto> bookings = bookingService.getOwnerBookings(owner.getId(), "CURRENT");
        assertTrue(bookings.isEmpty());
    }

    @Test
    void getOwnerBookings_PastState() {
        bookingRequestDto = new BookingRequestDto(item.getId(), LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1));
        bookingService.createBooking(booker.getId(), bookingRequestDto);
        List<BookingDto> bookings = bookingService.getOwnerBookings(owner.getId(), "PAST");
        assertFalse(bookings.isEmpty());
    }

    @Test
    void getOwnerBookings_FutureState() {
        bookingService.createBooking(booker.getId(), bookingRequestDto);
        List<BookingDto> bookings = bookingService.getOwnerBookings(owner.getId(), "FUTURE");
        assertFalse(bookings.isEmpty());
    }

    @Test
    void getOwnerBookings_WaitingState() {
        bookingService.createBooking(booker.getId(), bookingRequestDto);
        List<BookingDto> bookings = bookingService.getOwnerBookings(owner.getId(), "WAITING");
        assertFalse(bookings.isEmpty());
    }

    @Test
    void getOwnerBookings_RejectedState() {
        BookingDto bookingDto = bookingService.createBooking(booker.getId(), bookingRequestDto);
        bookingService.approveBooking(owner.getId(), bookingDto.getId(), false);
        List<BookingDto> bookings = bookingService.getOwnerBookings(owner.getId(), "REJECTED");
        assertFalse(bookings.isEmpty());
    }
}
