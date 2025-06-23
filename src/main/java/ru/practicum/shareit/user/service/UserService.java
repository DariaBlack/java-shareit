package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User createUser(User user);

    User updateUser(User user);

    User getUserById(Long userid);

    List<User> getAllUsers();

    void deleteUser(Long userId);
}
