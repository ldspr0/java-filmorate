package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film create(Film film);

    Film update(Film film);

    Collection<Film> findAll();

    void addLike(long filmId, long userId);

    void removeLike(long filmId, long userId);

    Collection<Film> getPopularFilms(int count);

}
