package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
@Getter
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private final UserStorage userStorage;

    @Override
    public Collection<Film> getAll() {
        log.trace("get all films from Memory: {}", films.values());
        return films.values();
    }

    @Override
    public Film create(Film film) {
        log.info("try to save film in Memory: {}", film.toString());
        film.setId(getNextId());
        film.setLikes(new HashSet<>());
        films.put(film.getId(), film);
        log.info("film is saved in Memory: {}", film);
        return film;
    }

    @Override
    public Film read(long id) {
        log.info("film returned from Memory: {}", films.get(id));
        return films.get(id);
    }

    @Override
    public Film update(Film newFilm) {
        log.info("try to update film in Memory: {}", newFilm.toString());
        Film oldFilm = films.get(newFilm.getId());
        oldFilm.setName(newFilm.getName());
        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());
        oldFilm.setDuration(newFilm.getDuration());
        log.info("film updated in Memory: {}", newFilm);
        return oldFilm;
    }

    @Override
    public boolean delete(long id) {
        if (read(id) == null) {
            return false;
        }
        films.remove(id);
        log.info("film with id {} is removed from Memory", id);
        return true;
    }

    @Override
    public boolean addLike(long filmId, long userId) {
        try {
            Film film = read(filmId);
            film.getLikes().add(userId);
            update(film);
            log.info("like from userId: {} is added to filmId: {} from Memory", userId, filmId);
            return true;
        } catch (RuntimeException e) {
            log.warn("issue with addLike with filmId: {} and userId: {}. error: {}", filmId, userId, e.getMessage());
            return false;
        }
    }

    @Override
    public boolean removeLike(long filmId, long userId) {
        try {
            Film film = read(filmId);
            film.getLikes().remove(userId);
            update(film);
            log.info("like from userId: {} is removed from filmId: {} from Memory", userId, filmId);
            return true;
        } catch (RuntimeException e) {
            log.warn("issue with removeLike with filmId: {} and userId: {}. error: {}", filmId, userId, e.getMessage());
            return false;
        }
    }

    @Override
    public Collection<Film> getPopularFilms(int count) {
        return films.values().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
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
