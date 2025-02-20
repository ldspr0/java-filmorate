package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

@Slf4j
@Repository
@Getter
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean addFriend(long userId, long friendId) {
        return false;
    }

    @Override
    public boolean removeFriend(long userId, long friendId) {
        return false;
    }

    @Override
    public Collection<User> getFriendList(long userId) {
        return List.of();
    }

    @Override
    public Collection<User> getCommonFriends(long userId, long secondUserId) {
        return List.of();
    }

    @Override
    public Collection<User> getAll() {
        return List.of();
    }

    @Override
    public User create(User entity) {
        return null;
    }

    @Override
    public User read(long id) {
        return null;
    }

    @Override
    public User update(User entity) {
        return null;
    }

    @Override
    public boolean delete(long id) {
        return false;
    }
}
