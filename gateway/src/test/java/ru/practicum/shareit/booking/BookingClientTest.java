package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingClientTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private RestTemplateBuilder restTemplateBuilder;

    @Mock
    private RestTemplate restTemplate;

    private BookingClient bookingClient;

    @BeforeEach
    void setUp() {
        when(restTemplateBuilder.uriTemplateHandler(any())).thenReturn(restTemplateBuilder);
        when(restTemplateBuilder.requestFactory(any(Supplier.class))).thenReturn(restTemplateBuilder);
        when(restTemplateBuilder.build()).thenReturn(restTemplate);

        bookingClient = new BookingClient("http://localhost:8080", restTemplateBuilder);
    }

    @Test
    void testGetBookings() {
        ResponseEntity<Object> expected = new ResponseEntity<>(HttpStatus.OK);
        Map<String, Object> params = Map.of(
                "state", BookingState.ALL.name(),
                "from", 0,
                "size", 10
        );

        when(restTemplate.exchange(
                eq("?state={state}&from={from}&size={size}"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class),
                eq(params)))
                .thenReturn(expected);

        ResponseEntity<Object> response =
                bookingClient.getBookings(1L, BookingState.ALL, 0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetBookingsByOwner() {
        ResponseEntity<Object> expected = new ResponseEntity<>(HttpStatus.OK);
        Map<String, Object> params = Map.of(
                "state", BookingState.WAITING.name(),
                "from", 5,
                "size", 20
        );

        when(restTemplate.exchange(
                eq("/owner?state={state}&from={from}&size={size}"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class),
                eq(params)))
                .thenReturn(expected);

        ResponseEntity<Object> response =
                bookingClient.getBookingsByOwner(2L, BookingState.WAITING, 5, 20);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testBookItem() {
        BookItemRequestDto dto = new BookItemRequestDto();
        ResponseEntity<Object> expected = new ResponseEntity<>(HttpStatus.CREATED);

        when(restTemplate.exchange(
                eq(""),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Object.class)))
                .thenReturn(expected);

        ResponseEntity<Object> response =
                bookingClient.bookItem(3L, dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testGetBooking() {
        ResponseEntity<Object> expected = new ResponseEntity<>(HttpStatus.OK);

        when(restTemplate.exchange(
                eq("/5"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class)))
                .thenReturn(expected);

        ResponseEntity<Object> response =
                bookingClient.getBooking(4L, 5L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testApproveBooking() {
        ResponseEntity<Object> expected = new ResponseEntity<>(HttpStatus.OK);
        Map<String, Object> params = Map.of("approved", true);

        when(restTemplate.exchange(
                eq("/6?approved={approved}"),
                eq(HttpMethod.PATCH),
                any(HttpEntity.class),
                eq(Object.class),
                eq(params)))
                .thenReturn(expected);

        ResponseEntity<Object> response =
                bookingClient.approveBooking(5L, 6L, true);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
