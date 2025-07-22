//package ru.practicum.shareit.item;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.DirtiesContext;
//import ru.practicum.shareit.exception.NotFoundException;
//import ru.practicum.shareit.item.dto.ItemDto;
//import ru.practicum.shareit.item.service.ItemService;
//import ru.practicum.shareit.user.dto.UserDto;
//import ru.practicum.shareit.user.service.UserService;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
//public class ItemIntegrationTest {
//
//    @Autowired
//    private ItemService itemService;
//
//    @Autowired
//    private UserService userService;
//
//    private UserDto testUser;
//    private ItemDto testItem;
//
//    @BeforeEach
//    void setUp() {
//        UserDto userToCreate = new UserDto();
//        userToCreate.setName("Тестовый пользователь");
//        userToCreate.setEmail("test" + System.currentTimeMillis() + "@example.com");
//        testUser = userService.createUser(userToCreate);
//
//        testItem = new ItemDto();
//        testItem.setName("Тестовая вещь");
//        testItem.setDescription("Описание тестовой вещи");
//        testItem.setAvailable(true);
//    }
//
//    @Test
//    void createAndGetItem() {
//        ItemDto createdItem = itemService.createItem(testUser.getId(), testItem);
//
//        assertNotNull(createdItem);
//        assertNotNull(createdItem.getId());
//        assertEquals(testItem.getName(), createdItem.getName());
//        assertEquals(testItem.getDescription(), createdItem.getDescription());
//        assertEquals(testItem.getAvailable(), createdItem.getAvailable());
//
//        ItemDto retrievedItem = itemService.getItemById(createdItem.getId());
//        assertEquals(createdItem.getId(), retrievedItem.getId());
//        assertEquals(createdItem.getName(), retrievedItem.getName());
//    }
//
//    @Test
//    void updateItem() {
//        ItemDto createdItem = itemService.createItem(testUser.getId(), testItem);
//
//        ItemDto updateDto = new ItemDto();
//        updateDto.setName("Обновленная вещь");
//        updateDto.setDescription("Обновленное описание");
//        updateDto.setAvailable(false);
//
//        ItemDto updatedItem = itemService.updateItem(testUser.getId(), createdItem.getId(), updateDto);
//
//        assertEquals(createdItem.getId(), updatedItem.getId());
//        assertEquals("Обновленная вещь", updatedItem.getName());
//        assertEquals("Обновленное описание", updatedItem.getDescription());
//        assertEquals(false, updatedItem.getAvailable());
//    }
//
//    @Test
//    void getItemsByUserId() {
//        itemService.createItem(testUser.getId(), testItem);
//
//        ItemDto secondItem = new ItemDto();
//        secondItem.setName("Вторая вещь");
//        secondItem.setDescription("Описание второй вещи");
//        secondItem.setAvailable(true);
//        itemService.createItem(testUser.getId(), secondItem);
//
//        List<ItemDto> userItems = itemService.getItemsByUserId(testUser.getId());
//
//        assertNotNull(userItems);
//        assertEquals(2, userItems.size());
//    }
//
//    @Test
//    void searchItems() {
//        ItemDto searchableItem = new ItemDto();
//        searchableItem.setName("Название для поиска");
//        searchableItem.setDescription("Описание");
//        searchableItem.setAvailable(true);
//        itemService.createItem(testUser.getId(), searchableItem);
//
//        List<ItemDto> foundItems = itemService.searchItems("Название");
//
//        assertNotNull(foundItems);
//        assertFalse(foundItems.isEmpty());
//        assertEquals("Название для поиска", foundItems.get(0).getName());
//
//        foundItems = itemService.searchItems("Описание");
//        assertNotNull(foundItems);
//        assertFalse(foundItems.isEmpty());
//
//        foundItems = itemService.searchItems("");
//        assertNotNull(foundItems);
//        assertTrue(foundItems.isEmpty());
//    }
//
//    @Test
//    void deleteItem() {
//        ItemDto createdItem = itemService.createItem(testUser.getId(), testItem);
//
//        itemService.deleteItem(testUser.getId(), createdItem.getId());
//
//        // Проверяем, что вещь удалена (должно выбросить исключение)
//        assertThrows(NotFoundException.class, () -> itemService.getItemById(createdItem.getId()));
//    }
//
//    @Test
//    void getNonExistentItem() {
//        // При запросе несуществующей вещи выбрасывается исключение
//        assertThrows(NotFoundException.class, () -> itemService.getItemById(9999L));
//    }
//
//    @Test
//    void updateItemByNonOwner() {
//        ItemDto createdItem = itemService.createItem(testUser.getId(), testItem);
//
//        UserDto anotherUser = new UserDto();
//        anotherUser.setName("Другой пользователь");
//        anotherUser.setEmail("another" + System.currentTimeMillis() + "@example.com");
//        UserDto createdAnotherUser = userService.createUser(anotherUser);
//
//        // Пытаемся обновить вещь от имени другого пользователя
//        ItemDto updateDto = new ItemDto();
//        updateDto.setName("Попытка обновления");
//
//        // Должно выбросить исключение
//        assertThrows(NotFoundException.class, () ->
//                itemService.updateItem(createdAnotherUser.getId(), createdItem.getId(), updateDto));
//    }
//}