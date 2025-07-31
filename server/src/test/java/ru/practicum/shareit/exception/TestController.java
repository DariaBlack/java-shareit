package ru.practicum.shareit.exception;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test/not-found")
    public void throwNotFoundException() {
        throw new NotFoundException("Не найдено");
    }

    @GetMapping("/test/already-exists")
    public void throwAlreadyExistsException() {
        throw new AlreadyExistsException("Уже существует");
    }

    @GetMapping("/test/access-denied")
    public void throwAccessDeniedException() {
        throw new AccessDeniedException("Доступ запрещён");
    }

    @GetMapping("/test/conflict")
    public void throwConflictException() {
        throw new ConflictException("Конфликт");
    }

    @GetMapping("/test/not-available")
    public void throwNotAvailableException() {
        throw new NotAvailableException("Недоступно");
    }

    @GetMapping("/test/exception")
    public void throwException() {
        throw new RuntimeException("Внутренняя ошибка");
    }
}
