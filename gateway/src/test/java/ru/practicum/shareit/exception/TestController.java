package ru.practicum.shareit.exception;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/test")
@Validated
public class TestController {

    @GetMapping("/illegal-argument")
    public void throwIllegalArgument() {
        throw new IllegalArgumentException("Недопустимый аргумент");
    }

    @GetMapping("/unexpected")
    public void throwUnexpected() {
        throw new RuntimeException("Произошла непредвиденная ошибка");
    }

    @PostMapping("/validate-body")
    public void validateBody(@Valid @RequestBody DummyDto dto) {

    }

    @GetMapping("/validate-param")
    public void validateParam(@RequestParam @NotBlank String name) {

    }

    @GetMapping("/validation-exception")
    public void throwValidationException() {
        throw new ValidationException("Ошибка проверки данных");
    }

    public static class DummyDto {
        @NotBlank
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
