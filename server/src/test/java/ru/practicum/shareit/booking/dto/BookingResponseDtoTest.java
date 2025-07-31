package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingResponseDtoTest {

    @Test
    void testBookingResponseDto() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);

        ItemDto item = new ItemDto();
        item.setId(1L);
        item.setName("Item Name");
        item.setDescription("Item Description");
        item.setAvailable(true);

        UserDto booker = new UserDto(1L, "Иван Иванов", "ivan.ivanov@example.com");
        BookingStatus status = BookingStatus.APPROVED;

        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setId(1L);
        bookingResponseDto.setStart(start);
        bookingResponseDto.setEnd(end);
        bookingResponseDto.setItem(item);
        bookingResponseDto.setBooker(booker);
        bookingResponseDto.setStatus(status);

        assertEquals(1L, bookingResponseDto.getId());
        assertEquals(start, bookingResponseDto.getStart());
        assertEquals(end, bookingResponseDto.getEnd());
        assertEquals(item, bookingResponseDto.getItem());
        assertEquals(booker, bookingResponseDto.getBooker());
        assertEquals(status, bookingResponseDto.getStatus());
    }
}
