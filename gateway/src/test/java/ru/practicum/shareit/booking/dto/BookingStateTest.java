package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookingStateTest {

    @Test
    void whenValidState_thenReturnOptionalOfState() {
        Optional<BookingState> state = BookingState.from("ALL");
        assertTrue(state.isPresent());
        assertEquals(BookingState.ALL, state.get());

        state = BookingState.from("current");
        assertTrue(state.isPresent());
        assertEquals(BookingState.CURRENT, state.get());

        state = BookingState.from("FuTuRe");
        assertTrue(state.isPresent());
        assertEquals(BookingState.FUTURE, state.get());

        state = BookingState.from("PAST");
        assertTrue(state.isPresent());
        assertEquals(BookingState.PAST, state.get());

        state = BookingState.from("rejected");
        assertTrue(state.isPresent());
        assertEquals(BookingState.REJECTED, state.get());

        state = BookingState.from("waiting");
        assertTrue(state.isPresent());
        assertEquals(BookingState.WAITING, state.get());
    }

    @Test
    void whenInvalidState_thenReturnEmptyOptional() {
        Optional<BookingState> state = BookingState.from("INVALID");
        assertTrue(state.isEmpty());

        state = BookingState.from("");
        assertTrue(state.isEmpty());

        state = BookingState.from(null);
        assertTrue(state.isEmpty());
    }
}
