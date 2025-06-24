package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    void createAndGetUser() {
        UserDto userToCreate = new UserDto();
        userToCreate.setName("Иван");
        userToCreate.setEmail("ivan" + System.currentTimeMillis() + "@example.com");

        UserDto createdUser = userService.createUser(userToCreate);

        assertNotNull(createdUser);
        assertNotNull(createdUser.getId());
        assertEquals(userToCreate.getName(), createdUser.getName());
        assertEquals(userToCreate.getEmail(), createdUser.getEmail());

        UserDto retrievedUser = userService.getUserById(createdUser.getId());
        assertEquals(createdUser.getId(), retrievedUser.getId());
        assertEquals(createdUser.getName(), retrievedUser.getName());
        assertEquals(createdUser.getEmail(), retrievedUser.getEmail());
    }

    @Test
    void updateUser() {
        UserDto userToCreate = new UserDto();
        userToCreate.setName("Петр");
        userToCreate.setEmail("petr" + System.currentTimeMillis() + "@example.com");

        UserDto createdUser = userService.createUser(userToCreate);

        UserDto updateDto = new UserDto();
        updateDto.setName("Обновленный Петр");
        updateDto.setEmail("updated.petr" + System.currentTimeMillis() + "@example.com");

        UserDto updatedUser = userService.updateUser(createdUser.getId(), updateDto);

        assertEquals(createdUser.getId(), updatedUser.getId());
        assertEquals(updateDto.getName(), updatedUser.getName());
        assertEquals(updateDto.getEmail(), updatedUser.getEmail());
    }

    @Test
    void getAllUsers() {
        UserDto user1 = new UserDto();
        user1.setName("Пользователь 1");
        user1.setEmail("user1" + System.currentTimeMillis() + "@example.com");
        userService.createUser(user1);

        UserDto user2 = new UserDto();
        user2.setName("Пользователь 2");
        user2.setEmail("user2" + System.currentTimeMillis() + "@example.com");
        userService.createUser(user2);

        List<UserDto> allUsers = userService.getAllUsers();

        assertNotNull(allUsers);
        assertFalse(allUsers.isEmpty());
        assertTrue(allUsers.size() >= 2);
    }

    @Test
    void deleteUser() {
        UserDto userToCreate = new UserDto();
        userToCreate.setName("Удаляемый пользователь");
        userToCreate.setEmail("to.delete" + System.currentTimeMillis() + "@example.com");

        UserDto createdUser = userService.createUser(userToCreate);

        userService.deleteUser(createdUser.getId());

        // Проверяем, что пользователь удален (должно выбросить исключение)
        assertThrows(NotFoundException.class, () -> userService.getUserById(createdUser.getId()));
    }

    @Test
    void getNonExistentUser() {
        // При запросе несуществующего пользователя выбрасывается исключение
        assertThrows(NotFoundException.class, () -> userService.getUserById(9999L));
    }

    @Test
    void partialUpdateUser() {
        UserDto userToCreate = new UserDto();
        userToCreate.setName("Первое имя");
        userToCreate.setEmail("original" + System.currentTimeMillis() + "@example.com");

        UserDto createdUser = userService.createUser(userToCreate);

        UserDto nameUpdateDto = new UserDto();
        nameUpdateDto.setName("Новое имя");

        UserDto nameUpdatedUser = userService.updateUser(createdUser.getId(), nameUpdateDto);

        // Проверяем, что имя обновлено, а email остался прежним
        assertEquals("Новое имя", nameUpdatedUser.getName());
        assertEquals(createdUser.getEmail(), nameUpdatedUser.getEmail());

        UserDto emailUpdateDto = new UserDto();
        emailUpdateDto.setEmail("new.email" + System.currentTimeMillis() + "@example.com");

        UserDto emailUpdatedUser = userService.updateUser(createdUser.getId(), emailUpdateDto);

        // Проверяем, что email обновлен, а имя осталось от предыдущего обновления
        assertEquals("Новое имя", emailUpdatedUser.getName());
        assertEquals(emailUpdateDto.getEmail(), emailUpdatedUser.getEmail());
    }
}