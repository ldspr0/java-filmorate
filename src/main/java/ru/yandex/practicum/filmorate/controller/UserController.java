package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private static final String MSG_ID_REQUIRED_VALIDATION_ERROR = "Id должен быть указан.";


    @GetMapping
    public Collection<User> findAll() {
        log.trace("get all users: {}", users.values());
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("input user: {}", user.toString());

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.trace("name is set to login: {}", user.getLogin());
        }

        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        log.info("input user: {}", newUser.toString());

        if (newUser.getId() == null) {
            log.warn(MSG_ID_REQUIRED_VALIDATION_ERROR);
            throw new ValidationException(MSG_ID_REQUIRED_VALIDATION_ERROR);
        }

        if (!users.containsKey(newUser.getId())) {
            log.warn("Пользователь с id = {} не найден", newUser.getId());
            throw new ValidationException("Пользователь с id = " + newUser.getId() + " не найден");
        }

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

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
