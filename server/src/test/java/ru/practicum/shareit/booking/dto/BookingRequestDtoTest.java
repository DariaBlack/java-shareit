package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingRequestDtoTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerialize() throws Exception {
        BookingRequestDto bookingRequestDto = new BookingRequestDto(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        String json = objectMapper.writeValueAsString(bookingRequestDto);
        assertThat(json).contains("\"itemId\":1");
    }

    @Test
    void testDeserialize() throws Exception {
        String json = "{\"itemId\":1,\"start\":\"2023-10-10T10:00:00\",\"end\":\"2023-10-11T10:00:00\"}";
        BookingRequestDto bookingRequestDto = objectMapper.readValue(json, BookingRequestDto.class);
        assertThat(bookingRequestDto.getItemId()).isEqualTo(1L);
    }
}