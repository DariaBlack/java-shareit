package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerialize() throws Exception {
        BookingDto bookingDto = new BookingDto(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), new ItemDto(), new UserDto(), BookingStatus.WAITING);
        String json = objectMapper.writeValueAsString(bookingDto);
        assertThat(json).contains("\"id\":1", "\"status\":\"WAITING\"");
    }

    @Test
    void testDeserialize() throws Exception {
        String json = "{\"id\":1,\"start\":\"2023-10-10T10:00:00\",\"end\":\"2023-10-11T10:00:00\",\"status\":\"WAITING\"}";
        BookingDto bookingDto = objectMapper.readValue(json, BookingDto.class);
        assertThat(bookingDto.getId()).isEqualTo(1L);
        assertThat(bookingDto.getStatus()).isEqualTo(BookingStatus.WAITING);
    }
}