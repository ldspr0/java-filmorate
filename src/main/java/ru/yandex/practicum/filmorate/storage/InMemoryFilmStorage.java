package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private static final String MSG_RELEASEDATE_VALIDATION_ERROR = "Дата релиза фильма не может быть раньше 28 декабря 1895 года.";
    private static final String MSG_ID_REQUIRED_VALIDATION_ERROR = "Id должен быть указан.";
    @Autowired
    UserStorage userStorage;

    @Override
    public Collection<Film> findAll() {
        log.trace("get all films: {}", films.values());
        return films.values();
    }

    @Override
    public Film create(Film film) {
        log.info("input film: {}", film.toString());

        if (!"Success".equals(validation(film))) {
            String errorMessage = validation(film);
            log.warn(errorMessage);
            throw new ValidationException(errorMessage);
        }

        film.setId(getNextId());
        film.setLikes(new HashSet<>());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        log.info("input film: {}", newFilm.toString());

        // проверяем необходимые условия
        if (newFilm.getId() == null) {
            log.warn(MSG_ID_REQUIRED_VALIDATION_ERROR);
            throw new ValidationException(MSG_ID_REQUIRED_VALIDATION_ERROR);
        }
        validateId(newFilm.getId());

        if (!"Success".equals(validation(newFilm))) {
            String errorMessage = validation(newFilm);
            log.warn(errorMessage);
            throw new ValidationException(errorMessage);
        }

        Film oldFilm = films.get(newFilm.getId());
        oldFilm.setName(newFilm.getName());
        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());
        oldFilm.setDuration(newFilm.getDuration());
        return oldFilm;
    }

    @Override
    public void addLike(long filmId, long userId) {
        Film film = getFilmById(filmId);
        userStorage.validateId(userId);

        film.getLikes().add(userId);
        update(film);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        Film film = getFilmById(filmId);
        userStorage.validateId(userId);

        film.getLikes().remove(userId);
        update(film);
    }

    @Override
    public Collection<Film> getPopularFilms(int count) {
        return films.values().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    private String validation(Film film) {
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            return MSG_RELEASEDATE_VALIDATION_ERROR;
        }

        return "Success";
    }

    private Film getFilmById(long id) {
        validateId(id);
        return films.get(id);
    }

    private void validateId(long id) {
        if (films.get(id) == null) {
            log.warn("Фильм с id = {} не найден", id);
            throw new NotFoundException("Фильм с id = " + id + " не найден");
        }
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
