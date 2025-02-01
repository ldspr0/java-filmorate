package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;


@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public void addLike(int id, int userId) {
        validateFilmId(id);
        validateUserId(userId);
        filmStorage.addLike(id, userId);
    }

    public void removeLike(int id, int userId) {
        validateFilmId(id);
        validateUserId(userId);
        filmStorage.removeLike(id, userId);
    }

    private void validateFilmId(int id) {
        if (filmStorage.read(id) == null) {
            log.warn("Фильм с id = {} не найден", id);
            throw new NotFoundException("Фильм с id = " + id + " не найден");
        }
    }

    private void validateUserId(int id) {
        if (userStorage.read(id) == null) {
            log.warn("Пользователь с id = {} не найден", id);
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
    }
}
