package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements ServiceLayer<User> {
    private final UserStorage userStorage;
    private static final String MSG_ID_REQUIRED_VALIDATION_ERROR = "Id должен быть указан.";

    @Override
    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    @Override
    public User create(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.trace("name is set to login: {}", user.getLogin());
        }

        return userStorage.create(user);
    }

    @Override
    public User read(long id) {
        User user = userStorage.read(id);
        if (user == null) {
            log.warn("Пользователь с id = {} не найден", id);
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }

        return user;
    }

    @Override
    public User update(User newUser) {
        if (newUser.getId() == null) {
            log.warn(MSG_ID_REQUIRED_VALIDATION_ERROR);
            throw new ValidationException(MSG_ID_REQUIRED_VALIDATION_ERROR);
        }

        read(newUser.getId());

        if (newUser.getName() == null || newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
            log.trace("name is set to login: {}", newUser.getLogin());
        }

        return userStorage.update(newUser);
    }

    @Override
    public boolean delete(long id) {
        read(id);
        return userStorage.delete(id);
    }

}
