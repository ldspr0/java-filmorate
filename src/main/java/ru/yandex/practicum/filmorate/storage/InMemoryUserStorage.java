package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> getAll() {
        log.trace("get all users from Memory: {}", users.values());
        return users.values();
    }

    @Override
    public User create(User user) {
        log.info("try to save user in Memory: {}", user.toString());
        user.setId(getNextId());
        user.setFriends(new HashSet<>());
        users.put(user.getId(), user);
        log.info("user is saved in Memory: {}", user);
        return user;
    }

    @Override
    public User read(long id) {
        return users.get(id);
    }

    @Override
    public User update(User newUser) {
        log.info("try to update user in Memory: {}", newUser.toString());
        User oldUser = users.get(newUser.getId());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setLogin(newUser.getLogin());
        oldUser.setName(newUser.getName());
        oldUser.setBirthday(newUser.getBirthday());
        log.info("user is updated in Memory: {}", newUser);
        return oldUser;
    }

    @Override
    public boolean delete(long id) {
        if (read(id) == null) {
            return false;
        }
        users.remove(id);
        log.info("user with id {} is removed from Memory", id);
        return true;
    }

    @Override
    public boolean addFriend(long userId, long friendId) {
        try {
            User user = read(userId);
            User friendUser = read(friendId);
            addFriend(user, friendId);
            addFriend(friendUser, userId);
            log.info("userId: {} is now a friend with friendId: {} ", userId, friendId);
            return true;
        } catch (RuntimeException e) {
            log.warn("issue with addFriend with userId: {} and friendId: {}. error: {}", userId, friendId, e.getMessage());
            return false;
        }
    }

    private void addFriend(User user, long friendId) {
        user.getFriends().add(friendId);
        update(user);
    }

    @Override
    public boolean removeFriend(long userId, long friendId) {
        try {
            User user = read(userId);
            User friendUser = read(friendId);
            removeFriend(user, friendId);
            removeFriend(friendUser, userId);
            log.info("userId: {} and friendId: {} not a friends anymore ", userId, friendId);
            return true;
        } catch (RuntimeException e) {
            log.warn("issue with removeFriend with userId: {} and friendId: {}. error: {}", userId, friendId, e.getMessage());
            return false;
        }

    }

    private void removeFriend(User user, long friendId) {
        user.getFriends().remove(friendId);
        update(user);
    }

    @Override
    public Collection<User> getFriendList(long userId) {
        return users.values().stream()
                .filter(user -> user.getFriends() != null && user.getFriends().contains(userId))
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<User> getCommonFriends(long userId, long secondUserId) {
        return users.values().stream()
                .filter(user -> user.getFriends() != null && user.getFriends().contains(userId) && user.getFriends().contains(secondUserId))
                .collect(Collectors.toSet());
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
