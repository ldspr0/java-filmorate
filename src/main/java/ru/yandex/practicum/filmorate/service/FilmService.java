package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService implements ServiceLayer<Film> {
    private final FilmStorage filmStorage;
    private static final String MSG_RELEASEDATE_VALIDATION_ERROR = "Дата релиза фильма не может быть раньше 28 декабря 1895 года.";
    private static final String MSG_ID_REQUIRED_VALIDATION_ERROR = "Id должен быть указан.";

    @Override
    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    @Override
    public Film read(long id) {
        Film film = filmStorage.read(id);
        if (film == null) {
            log.warn("Фильм с id = {} не найден", id);
            throw new NotFoundException("Фильм с id = " + id + " не найден");
        }
        return film;
    }

    @Override
    public Film create(Film film) {
        if (!"Success".equals(validation(film))) {
            String errorMessage = validation(film);
            log.warn(errorMessage);
            throw new ValidationException(errorMessage);
        }

        return filmStorage.create(film);
    }

    @Override
    public Film update(Film newFilm) {
        if (newFilm.getId() == null) {
            log.warn(MSG_ID_REQUIRED_VALIDATION_ERROR);
            throw new ValidationException(MSG_ID_REQUIRED_VALIDATION_ERROR);
        }
        read(newFilm.getId());

        if (!"Success".equals(validation(newFilm))) {
            String errorMessage = validation(newFilm);
            log.warn(errorMessage);
            throw new ValidationException(errorMessage);
        }

        return filmStorage.update(newFilm);
    }

    @Override
    public boolean delete(long id) {
        read(id);
        return filmStorage.delete(id);
    }

    public Collection<Film> getPopular(int count) {
        return filmStorage.getPopularFilms(count);
    }

    private String validation(Film film) {
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            return MSG_RELEASEDATE_VALIDATION_ERROR;
        }

        return "Success";
    }

}
