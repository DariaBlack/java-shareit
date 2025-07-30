package ru.practicum.shareit.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class BaseClientTest {

    private RestTemplate restTemplate;
    private BaseClient baseClient;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        baseClient = new BaseClient(restTemplate);
    }

    @Test
    void testGetRequest() {
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class), any(Map.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> response = baseClient.get("/test", 1L, Collections.emptyMap());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testPostRequest() {
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.CREATED);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Object.class), any(Map.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> response = baseClient.post("/test", 1L, Collections.emptyMap(), "body");

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testPutRequest() {
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(HttpEntity.class), eq(Object.class), any(Map.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> response = baseClient.put("/test", 1L, Collections.emptyMap(), "body");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testPatchRequest() {
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PATCH), any(HttpEntity.class), eq(Object.class), any(Map.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> response = baseClient.patch("/test", 1L, Collections.emptyMap(), "body");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteRequest() {
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.DELETE), any(HttpEntity.class), eq(Object.class), any(Map.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> response = baseClient.delete("/test", 1L, Collections.emptyMap());

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testHandleHttpStatusCodeException() {
        HttpStatusCodeException exception = mock(HttpStatusCodeException.class);
        when(exception.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);
        when(exception.getResponseBodyAsByteArray()).thenReturn(new byte[0]);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class), any(Map.class)))
                .thenThrow(exception);

        ResponseEntity<Object> response = baseClient.get("/test", 1L, Collections.emptyMap());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetWithoutUserId() {
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> response = baseClient.get("/test");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testPostWithoutUserId() {
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.CREATED);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Object.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> response = baseClient.post("/test", "body");

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testPatchWithoutUserId() {
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PATCH), any(HttpEntity.class), eq(Object.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> response = baseClient.patch("/test", "body");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteWithoutUserId() {
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.DELETE), any(HttpEntity.class), eq(Object.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> response = baseClient.delete("/test");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
