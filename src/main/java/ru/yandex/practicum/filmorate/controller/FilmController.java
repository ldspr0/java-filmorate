package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();
    private static final String MSG_RELEASEDATE_VALIDATION_ERROR = "Дата релиза фильма не может быть раньше 28 декабря 1895 года.";
    private static final String MSG_ID_REQUIRED_VALIDATION_ERROR = "Id должен быть указан.";

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.trace("input film:");
        log.trace(film.toString());
        // проверяем выполнение необходимых условий
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn(MSG_RELEASEDATE_VALIDATION_ERROR);
            throw new ValidationException(MSG_RELEASEDATE_VALIDATION_ERROR);
        }

        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }


    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        log.trace("input film:");
        log.trace(newFilm.toString());
        // проверяем необходимые условия
        if (newFilm.getId() == null) {
            log.warn(MSG_ID_REQUIRED_VALIDATION_ERROR);
            throw new ValidationException(MSG_ID_REQUIRED_VALIDATION_ERROR);
        }
        // проверяем выполнение необходимых условий
        if (newFilm.getReleaseDate() == null || newFilm.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn(MSG_RELEASEDATE_VALIDATION_ERROR);
            throw new ValidationException(MSG_RELEASEDATE_VALIDATION_ERROR);
        }

        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            oldFilm.setDuration(newFilm.getDuration());

            return oldFilm;
        }

        log.warn("Фильм с id = " + newFilm.getId() + " не найден");
        throw new ValidationException("Фильм с id = " + newFilm.getId() + " не найден");
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
