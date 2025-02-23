package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.rowmapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static ru.yandex.practicum.filmorate.constants.SqlConstants.*;

@Slf4j
@Repository
@RequiredArgsConstructor
@Primary
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Film> filmRowMapper = new FilmRowMapper();

    @Override
    public boolean addLike(long filmId, long userId) {
        int rowsAffected = jdbcTemplate.update(INSERT_LIKE, filmId, userId, LocalDate.now(), LocalDate.now());
        return rowsAffected > 0;
    }

    @Override
    public boolean removeLike(long filmId, long userId) {
        int rowsAffected = jdbcTemplate.update(DELETE_LIKE, filmId, userId);
        return rowsAffected > 0;
    }

    @Override
    public Collection<Film> getPopularFilms(int count) {
        return jdbcTemplate.query(GET_POPULAR_FILMS, filmRowMapper, count);

    }

    @Override
    public Collection<Film> getAll() {
        return jdbcTemplate.query(GET_FILMS + ADD_LIMIT, filmRowMapper);
    }


    @Override
    @Transactional
    public Film create(Film entity) {
        Date releaseDate = Date.valueOf(entity.getReleaseDate());
        Timestamp timestampNow = Timestamp.valueOf(LocalDateTime.now());
        List<Object> params = Arrays.asList(entity.getName(),
                entity.getDescription(),
                entity.getMpa().getId(),
                entity.getDuration(),
                releaseDate,
                timestampNow,
                timestampNow
        );
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(INSERT_FILMS, Statement.RETURN_GENERATED_KEYS);
            for (int idx = 0; idx < params.size(); idx++) {
                ps.setObject(idx + 1, params.get(idx));
            }
            return ps;
        }, keyHolder);

        Long filmId = keyHolder.getKeyAs(Long.class);

        if (filmId != null) {
            entity.setId(filmId);
        } else {
            throw new InternalServerException("Не удалось сохранить данные");
        }
        entity.setId(filmId);

        // Добавляем жанры в таблицу film_genre
        if (entity.getGenres() != null && !entity.getGenres().isEmpty()) {
            for (Genre genre : entity.getGenres()) {
                jdbcTemplate.update(
                        INSERT_FILM_GENRES,
                        filmId,
                        genre.getId(),
                        timestampNow,
                        timestampNow
                );
            }
        }

        return entity;
    }

    @Override
    public Film read(long id) {
        try {
            return jdbcTemplate.queryForObject(GET_FILMS_BY_ID + ADD_LIMIT, filmRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Жанр с таким Id не был найден");
        }
    }

    @Override
    public Film update(Film entity) {
        Date releaseDate = Date.valueOf(entity.getReleaseDate());
        Timestamp updatedAt = Timestamp.valueOf(LocalDateTime.now());

        List<Object> params = Arrays.asList(entity.getName(),
                entity.getDescription(),
                entity.getMpa().getId(),
                entity.getDuration(),
                releaseDate,
                updatedAt,
                entity.getId()
        );
        int rowsUpdated = jdbcTemplate.update(UPDATE_FILMS, params.toArray());
        if (rowsUpdated == 0) {
            throw new InternalServerException("Не удалось обновить данные");
        }
        return entity;
    }

    @Override
    public boolean delete(long id) {
        return jdbcTemplate.update(DELETE_FILMS, id) > 0;
    }

}

