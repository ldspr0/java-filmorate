package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage extends Storage<User> {

    boolean addFriend(long userId, long friendId);

    boolean removeFriend(long userId, long friendId);

    Collection<User> getFriendList(long userId);

    Collection<User> getCommonFriends(long userId, long secondUserId);
}
