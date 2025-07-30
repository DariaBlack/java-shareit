package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TestController.class)
@Import(ErrorHandler.class)
class ErrorHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void handleNotFoundException() throws Exception {
        mockMvc.perform(get("/test/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Не найдено"));
    }

    @Test
    void handleAlreadyExistsException() throws Exception {
        mockMvc.perform(get("/test/already-exists"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Уже существует"));
    }

    @Test
    void handleAccessDeniedException() throws Exception {
        mockMvc.perform(get("/test/access-denied"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Доступ запрещён"));
    }

    @Test
    void handleException() throws Exception {
        mockMvc.perform(get("/test/exception"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Внутренняя ошибка"));
    }

    @Test
    void handleNotAvailableException() throws Exception {
        mockMvc.perform(get("/test/not-available"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Недоступно"));
    }

    @Test
    void handleConflictException() throws Exception {
        mockMvc.perform(get("/test/conflict"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Конфликт"));
    }

    @Test
    void handleBadRequestException() throws Exception {
        mockMvc.perform(get("/test/bad-request"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Некорректный запрос"));
    }
}
