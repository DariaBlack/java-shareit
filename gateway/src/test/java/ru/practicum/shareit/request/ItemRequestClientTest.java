package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestClientTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private RestTemplateBuilder restTemplateBuilder;

    @Mock
    private RestTemplate restTemplate;

    private ItemRequestClient itemRequestClient;

    @BeforeEach
    void setUp() {
        when(restTemplateBuilder.uriTemplateHandler(any())).thenReturn(restTemplateBuilder);
        when(restTemplateBuilder.requestFactory(any(Supplier.class))).thenReturn(restTemplateBuilder);
        when(restTemplateBuilder.build()).thenReturn(restTemplate);

        itemRequestClient = new ItemRequestClient("http://localhost:8080", restTemplateBuilder);
    }

    @Test
    void testCreateItemRequest() {
        ItemRequestCreateDto dto = new ItemRequestCreateDto();
        ResponseEntity<Object> expected = new ResponseEntity<>(HttpStatus.CREATED);

        when(restTemplate.exchange(
                eq(""),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Object.class)))
                .thenReturn(expected);

        ResponseEntity<Object> response = itemRequestClient.createItemRequest(42L, dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testGetItemRequests() {
        ResponseEntity<Object> expected = new ResponseEntity<>(HttpStatus.OK);

        when(restTemplate.exchange(
                eq(""),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class)))
                .thenReturn(expected);

        ResponseEntity<Object> response = itemRequestClient.getItemRequests(42L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetAllItemRequests() {
        ResponseEntity<Object> expected = new ResponseEntity<>(HttpStatus.OK);

        when(restTemplate.exchange(
                eq("/all?from={from}&size={size}"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class),
                any(Map.class)))
                .thenReturn(expected);

        ResponseEntity<Object> response = itemRequestClient.getAllItemRequests(42L, 5, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetItemRequestById() {
        ResponseEntity<Object> expected = new ResponseEntity<>(HttpStatus.OK);

        when(restTemplate.exchange(
                eq("/3"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class)))
                .thenReturn(expected);

        ResponseEntity<Object> response = itemRequestClient.getItemRequestById(42L, 3L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
