package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private static final String MSG_LOGIN_VALIDATION_ERROR = "Логин не может быть пустым и содержать пробелы.";
    private static final String MSG_BIRTHDAY_VALIDATION_ERROR = "Дата рождения не может быть в будущем.";
    private static final String MSG_ID_REQUIRED_VALIDATION_ERROR = "Id должен быть указан.";


    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.trace("input user:");
        log.trace(user.toString());
        // проверяем выполнение необходимых условий
        if (user.getLogin() == null || user.getLogin().trim().contains(" ")) {
            log.warn(MSG_LOGIN_VALIDATION_ERROR);
            throw new ValidationException(MSG_LOGIN_VALIDATION_ERROR);
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            log.warn(MSG_BIRTHDAY_VALIDATION_ERROR);
            throw new ValidationException(MSG_BIRTHDAY_VALIDATION_ERROR);
        }

        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }


    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        log.trace("input user:");
        log.trace(newUser.toString());
        // проверяем необходимые условия
        if (newUser.getId() == null) {
            log.warn(MSG_ID_REQUIRED_VALIDATION_ERROR);
            throw new ValidationException(MSG_ID_REQUIRED_VALIDATION_ERROR);
        }
        if (newUser.getLogin() == null || newUser.getLogin().trim().contains(" ")) {
            log.warn(MSG_LOGIN_VALIDATION_ERROR);
            throw new ValidationException(MSG_LOGIN_VALIDATION_ERROR);
        }
        if (newUser.getBirthday() != null && newUser.getBirthday().isAfter(LocalDate.now())) {
            log.warn(MSG_BIRTHDAY_VALIDATION_ERROR);
            throw new ValidationException(MSG_BIRTHDAY_VALIDATION_ERROR);
        }

        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());

            if (newUser.getName() == null || newUser.getName().isBlank()) {
                oldUser.setName(oldUser.getLogin());
            } else {
                oldUser.setName(newUser.getName());
            }

            if (newUser.getBirthday() != null) {
                oldUser.setBirthday(newUser.getBirthday());
            }

            return oldUser;
        }

        log.warn("Пользователь с id = " + newUser.getId() + " не найден");
        throw new ValidationException("Пользователь с id = " + newUser.getId() + " не найден");
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
