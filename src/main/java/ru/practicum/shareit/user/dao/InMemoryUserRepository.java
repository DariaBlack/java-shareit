package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.AlreadyExistsException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            checkEmailUnique(user.getEmail(), null);
            user.setId(getId());
        } else {
            checkEmailUnique(user.getEmail(), user.getId());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteById(Long id) {
        users.remove(id);
    }

    private void checkEmailUnique(String email, Long userId) {
        for (User existingUser : users.values()) {
            if (existingUser.getEmail().equals(email) && (userId == null || !existingUser.getId().equals(userId))) {
                throw new AlreadyExistsException(("Пользователь с email " + email + " уже существует"));
            }
        }
    }

    private long getId() {
        long lastId = users.values().stream()
                .mapToLong(User::getId)
                .max()
                .orElse(0);
        return lastId + 1;
    }
}
