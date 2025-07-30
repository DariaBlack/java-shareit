package ru.practicum.shareit.booking.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookItemRequestDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsAreValid_thenNoConstraintViolations() {
        BookItemRequestDto dto = new BookItemRequestDto(
                1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        Set<ConstraintViolation<BookItemRequestDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void whenItemIdIsNull_thenConstraintViolation() {
        BookItemRequestDto dto = new BookItemRequestDto(
                null,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        Set<ConstraintViolation<BookItemRequestDto>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("ID вещи должен быть указан", violations.iterator().next().getMessage());
    }

    @Test
    void whenStartDateIsInThePast_thenConstraintViolation() {
        BookItemRequestDto dto = new BookItemRequestDto(
                1L,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        Set<ConstraintViolation<BookItemRequestDto>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("Дата начала бронирования не может быть в прошлом", violations.iterator().next().getMessage());
    }

    @Test
    void whenEndDateIsBeforeStartDate_thenConstraintViolation() {
        BookItemRequestDto dto = new BookItemRequestDto(
                1L,
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(1)
        );

        Set<ConstraintViolation<BookItemRequestDto>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("Дата окончания должна быть позже даты начала", violations.iterator().next().getMessage());
    }

    @Test
    void whenEndDateIsNull_thenConstraintViolation() {
        BookItemRequestDto dto = new BookItemRequestDto(
                1L,
                LocalDateTime.now().plusDays(1),
                null
        );

        Set<ConstraintViolation<BookItemRequestDto>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("Дата окончания бронирования должна быть указана", violations.iterator().next().getMessage());
    }
}
