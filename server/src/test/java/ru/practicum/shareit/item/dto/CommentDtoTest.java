package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentDtoTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerialize() throws Exception {
        CommentDto commentDto = new CommentDto(1L, "Test comment", "Author", LocalDateTime.now());
        String json = objectMapper.writeValueAsString(commentDto);
        assertThat(json).contains("\"text\":\"Test comment\"");
    }

    @Test
    void testDeserialize() throws Exception {
        String json = "{\"id\":1,\"text\":\"Test comment\",\"authorName\":\"Author\",\"created\":\"2023-10-10T10:00:00\"}";
        CommentDto commentDto = objectMapper.readValue(json, CommentDto.class);
        assertThat(commentDto.getText()).isEqualTo("Test comment");
    }
}
