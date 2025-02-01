package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage extends Storage<Film> {

    boolean addLike(long filmId, long userId);

    boolean removeLike(long filmId, long userId);

    Collection<Film> getPopularFilms(int count);

}
