package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private static final String MSG_ID_REQUIRED_VALIDATION_ERROR = "Id должен быть указан.";

    @Override
    public Collection<User> findAll() {
        log.trace("get all users: {}", users.values());
        return users.values();
    }

    @Override
    public User create(User user) {
        log.info("input user: {}", user.toString());

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.trace("name is set to login: {}", user.getLogin());
        }

        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User newUser) {
        log.info("input user: {}", newUser.toString());

        if (newUser.getId() == null) {
            log.warn(MSG_ID_REQUIRED_VALIDATION_ERROR);
            throw new ValidationException(MSG_ID_REQUIRED_VALIDATION_ERROR);
        }

        validateId(newUser.getId());

        User oldUser = users.get(newUser.getId());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setLogin(newUser.getLogin());

        if (newUser.getName() == null || newUser.getName().isBlank()) {
            oldUser.setName(oldUser.getLogin());
            log.trace("name is set to login: {}", oldUser.getLogin());
        } else {
            oldUser.setName(newUser.getName());
        }

        if (newUser.getBirthday() != null) {
            oldUser.setBirthday(newUser.getBirthday());
        }

        return oldUser;
    }

    @Override
    public void addFriend(long userId, long friendId) {
        User user = getUserById(userId);
        User friendUser = getUserById(friendId);

        addFriend(user, friendId);
        addFriend(friendUser, userId);
    }

    private void addFriend(User user, long friendId) {
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }

        user.getFriends().add(friendId);
        update(user);
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        User user = getUserById(userId);
        User friendUser = getUserById(friendId);

        removeFriend(user, friendId);
        removeFriend(friendUser, userId);
    }

    private void removeFriend(User user, long friendId) {
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }

        user.getFriends().remove(friendId);
        update(user);
    }

    @Override
    public Collection<User> getFriendList(long userId) {
        validateId(userId);

        return users.values().stream()
                .filter(user -> user.getFriends() != null && user.getFriends().contains(userId))
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<User> getCommonFriends(long userId, long secondUserId) {
        validateId(userId);
        validateId(secondUserId);

        return users.values().stream()
                .filter(user -> user.getFriends() != null && user.getFriends().contains(userId) && user.getFriends().contains(secondUserId))
                .collect(Collectors.toSet());
    }

    private User getUserById(long id) {
        validateId(id);
        return users.get(id);
    }

    @Override
    public void validateId(long id) {
        if (users.get(id) == null) {
            log.warn("Пользователь с id = {} не найден", id);
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
