package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserClientTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private RestTemplateBuilder restTemplateBuilder;

    @Mock
    private RestTemplate restTemplate;

    private UserClient userClient;

    @BeforeEach
    void setUp() {
        when(restTemplateBuilder.uriTemplateHandler(any())).thenReturn(restTemplateBuilder);
        when(restTemplateBuilder.requestFactory(any(Supplier.class))).thenReturn(restTemplateBuilder);
        when(restTemplateBuilder.build()).thenReturn(restTemplate);

        userClient = new UserClient("http://localhost:8080", restTemplateBuilder);
    }

    @Test
    void testCreateUser() {
        UserDto userDto = new UserDto();
        ResponseEntity<Object> expected = new ResponseEntity<>(HttpStatus.CREATED);

        when(restTemplate.exchange(
                eq(""),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Object.class)))
                .thenReturn(expected);

        ResponseEntity<Object> response = userClient.createUser(userDto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testUpdateUser() {
        UserDto userDto = new UserDto();
        ResponseEntity<Object> expected = new ResponseEntity<>(HttpStatus.OK);

        when(restTemplate.exchange(
                eq("/1"),
                eq(HttpMethod.PATCH),
                any(HttpEntity.class),
                eq(Object.class)))
                .thenReturn(expected);

        ResponseEntity<Object> response = userClient.updateUser(1L, userDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetUserById() {
        ResponseEntity<Object> expected = new ResponseEntity<>(HttpStatus.OK);

        when(restTemplate.exchange(
                eq("/1"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class)))
                .thenReturn(expected);

        ResponseEntity<Object> response = userClient.getUserById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetAllUsers() {
        ResponseEntity<Object> expected = new ResponseEntity<>(HttpStatus.OK);

        when(restTemplate.exchange(
                eq(""),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class)))
                .thenReturn(expected);

        ResponseEntity<Object> response = userClient.getAllUsers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteUser() {
        ResponseEntity<Object> expected = new ResponseEntity<>(HttpStatus.NO_CONTENT);

        when(restTemplate.exchange(
                eq("/1"),
                eq(HttpMethod.DELETE),
                any(HttpEntity.class),
                eq(Object.class)))
                .thenReturn(expected);

        ResponseEntity<Object> response = userClient.deleteUser(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
