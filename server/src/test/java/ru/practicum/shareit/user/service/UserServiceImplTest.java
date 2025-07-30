package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto(null, "Test User", "test@example.com");
    }

    @Test
    void createUser() {
        UserDto createdUser = userService.createUser(userDto);
        assertNotNull(createdUser.getId());
        assertEquals(userDto.getName(), createdUser.getName());
        assertEquals(userDto.getEmail(), createdUser.getEmail());
    }

    @Test
    void updateUser() {
        UserDto createdUser = userService.createUser(userDto);
        createdUser.setName("Updated Name");
        UserDto updatedUser = userService.updateUser(createdUser.getId(), createdUser);
        assertEquals("Updated Name", updatedUser.getName());
    }

    @Test
    void updateUserWithNonExistentId() {
        assertThrows(NotFoundException.class, () -> userService.updateUser(999L, userDto));
    }

    @Test
    void getUserById() {
        UserDto createdUser = userService.createUser(userDto);
        UserDto foundUser = userService.getUserById(createdUser.getId());
        assertEquals(createdUser.getId(), foundUser.getId());
    }

    @Test
    void getUserByIdNotFound() {
        assertThrows(NotFoundException.class, () -> userService.getUserById(999L));
    }

    @Test
    void getAllUsers() {
        userService.createUser(userDto);
        assertFalse(userService.getAllUsers().isEmpty());
    }

    @Test
    void deleteUser() {
        UserDto createdUser = userService.createUser(userDto);
        userService.deleteUser(createdUser.getId());
        assertThrows(NotFoundException.class, () -> userService.getUserById(createdUser.getId()));
    }

    @Test
    void deleteUserNotFound() {
        assertThrows(NotFoundException.class, () -> userService.deleteUser(999L));
    }
}
