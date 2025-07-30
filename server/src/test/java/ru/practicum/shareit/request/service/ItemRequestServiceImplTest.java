package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemRequestServiceImplTest {

    @Autowired
    private ItemRequestServiceImpl itemRequestService;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private ItemRequestCreateDto itemRequestCreateDto;

    @BeforeEach
    void setUp() {
        user = new User(null, "Test User", "test@example.com");
        user = userRepository.save(user);
        itemRequestCreateDto = new ItemRequestCreateDto("Test Request");
    }

    @Test
    void createItemRequest() {
        ItemRequestDto createdRequest = itemRequestService.createItemRequest(user.getId(), itemRequestCreateDto);
        assertNotNull(createdRequest.getId());
        assertEquals(itemRequestCreateDto.getDescription(), createdRequest.getDescription());
    }

    @Test
    void getItemRequestsByUserId() {
        itemRequestService.createItemRequest(user.getId(), itemRequestCreateDto);
        List<ItemRequestDto> requests = itemRequestService.getItemRequestsByUserId(user.getId());
        assertFalse(requests.isEmpty());
    }

    @Test
    void getAllItemRequests() {
        itemRequestService.createItemRequest(user.getId(), itemRequestCreateDto);
        List<ItemRequestDto> requests = itemRequestService.getAllItemRequests(user.getId(), 0, 10);
        assertTrue(requests.isEmpty()); // Assuming no other users have requests
    }

    @Test
    void getItemRequestById() {
        ItemRequestDto createdRequest = itemRequestService.createItemRequest(user.getId(), itemRequestCreateDto);
        ItemRequestDto foundRequest = itemRequestService.getItemRequestById(user.getId(), createdRequest.getId());
        assertEquals(createdRequest.getId(), foundRequest.getId());
    }

    @Test
    void getItemRequestById_NotFound() {
        assertThrows(NotFoundException.class, () -> itemRequestService.getItemRequestById(user.getId(), 999L));
    }
}