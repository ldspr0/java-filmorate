package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendService {
    private final UserStorage userStorage;

    public void addFriend(int id, int friendId) {
        validateId(id);
        validateId(friendId);
        userStorage.addFriend(id, friendId);
    }

    public void removeFriend(int id, int friendId) {
        validateId(id);
        validateId(friendId);
        userStorage.removeFriend(id, friendId);
    }

    public Collection<User> getFriends(int id) {
        validateId(id);
        return userStorage.getFriendList(id);
    }

    public Collection<User> getCommonFriends(int id, int otherId) {
        validateId(id);
        validateId(otherId);
        return userStorage.getCommonFriends(id, otherId);
    }

    private void validateId(int id) {
        if (userStorage.read(id) == null) {
            log.warn("Пользователь с id = {} не найден", id);
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
    }
}
