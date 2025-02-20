package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

@Slf4j
@Repository
@Getter
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean addLike(long filmId, long userId) {
        return false;
    }

    @Override
    public boolean removeLike(long filmId, long userId) {
        return false;
    }

    @Override
    public Collection<Film> getPopularFilms(int count) {
        return List.of();
    }

    @Override
    public Collection<Film> getAll() {
        return List.of();
    }

    @Override
    public Film create(Film entity) {
        return null;
    }

    @Override
    public Film read(long id) {
        return null;
    }

    @Override
    public Film update(Film entity) {
        return null;
    }

    @Override
    public boolean delete(long id) {
        return false;
    }
}

