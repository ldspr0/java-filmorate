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
        log.trace("get all films: {}", films.values());
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("input film: {}", film.toString());

        if (!"Success".equals(validation(film))) {
            String errorMessage = validation(film);
            log.warn(errorMessage);
            throw new ValidationException(errorMessage);
        }

        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }


    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        log.info("input film: {}", newFilm.toString());

        // проверяем необходимые условия
        if (newFilm.getId() == null) {
            log.warn(MSG_ID_REQUIRED_VALIDATION_ERROR);
            throw new ValidationException(MSG_ID_REQUIRED_VALIDATION_ERROR);
        }
        if (!films.containsKey(newFilm.getId())) {
            log.warn("Фильм с id = {} не найден", newFilm.getId());
            throw new ValidationException("Фильм с id = " + newFilm.getId() + " не найден");
        }

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

    private String validation(Film film) {
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            return MSG_RELEASEDATE_VALIDATION_ERROR;
        }

        return "Success";
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
