package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDtoTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerialize() throws Exception {
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "Test Request", LocalDateTime.now(), Collections.emptyList());
        String json = objectMapper.writeValueAsString(itemRequestDto);
        assertThat(json).contains("\"id\":1", "\"description\":\"Test Request\"");
    }

    @Test
    void testDeserialize() throws Exception {
        String json = "{\"id\":1,\"description\":\"Test Request\",\"created\":\"2023-10-10T10:00:00\",\"items\":[]}";
        ItemRequestDto itemRequestDto = objectMapper.readValue(json, ItemRequestDto.class);
        assertThat(itemRequestDto.getId()).isEqualTo(1L);
        assertThat(itemRequestDto.getDescription()).isEqualTo("Test Request");
    }
}