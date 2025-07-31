package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemClientTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private RestTemplateBuilder restTemplateBuilder;

    @Mock
    private RestTemplate restTemplate;

    private ItemClient itemClient;

    @BeforeEach
    void setUp() {
        when(restTemplateBuilder.uriTemplateHandler(any())).thenReturn(restTemplateBuilder);
        when(restTemplateBuilder.requestFactory(any(Supplier.class))).thenReturn(restTemplateBuilder);
        when(restTemplateBuilder.build()).thenReturn(restTemplate);

        itemClient = new ItemClient("http://localhost:8080", restTemplateBuilder);
    }

    @Test
    void testCreateItem() {
        ItemDto dto = new ItemDto();
        ResponseEntity<Object> expected = new ResponseEntity<>(HttpStatus.CREATED);

        when(restTemplate.exchange(
                eq(""),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Object.class)))
                .thenReturn(expected);

        ResponseEntity<Object> response = itemClient.createItem(7L, dto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testUpdateItem() {
        ItemDto dto = new ItemDto();
        ResponseEntity<Object> expected = new ResponseEntity<>(HttpStatus.OK);

        when(restTemplate.exchange(
                eq("/5"),
                eq(HttpMethod.PATCH),
                any(HttpEntity.class),
                eq(Object.class)))
                .thenReturn(expected);

        ResponseEntity<Object> response = itemClient.updateItem(7L, 5L, dto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetItemById() {
        ResponseEntity<Object> expected = new ResponseEntity<>(HttpStatus.OK);

        when(restTemplate.exchange(
                eq("/5"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class)))
                .thenReturn(expected);

        ResponseEntity<Object> response = itemClient.getItemById(7L, 5L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetItemsByUserId() {
        ResponseEntity<Object> expected = new ResponseEntity<>(HttpStatus.OK);

        when(restTemplate.exchange(
                eq(""),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class)))
                .thenReturn(expected);

        ResponseEntity<Object> response = itemClient.getItemsByUserId(7L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testSearchItems() {
        ResponseEntity<Object> expected = new ResponseEntity<>(HttpStatus.OK);

        when(restTemplate.exchange(
                eq("/search?text={text}"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class),
                eq(Map.of("text", "hammer"))))
                .thenReturn(expected);

        ResponseEntity<Object> response = itemClient.searchItems("hammer");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteItem() {
        ResponseEntity<Object> expected = new ResponseEntity<>(HttpStatus.NO_CONTENT);

        when(restTemplate.exchange(
                eq("/5"),
                eq(HttpMethod.DELETE),
                any(HttpEntity.class),
                eq(Object.class)))
                .thenReturn(expected);

        ResponseEntity<Object> response = itemClient.deleteItem(7L, 5L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testAddComment() {
        CommentRequestDto commentDto = new CommentRequestDto();
        ResponseEntity<Object> expected = new ResponseEntity<>(HttpStatus.OK);

        when(restTemplate.exchange(
                eq("/5/comment"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Object.class)))
                .thenReturn(expected);

        ResponseEntity<Object> response = itemClient.addComment(7L, 5L, commentDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
