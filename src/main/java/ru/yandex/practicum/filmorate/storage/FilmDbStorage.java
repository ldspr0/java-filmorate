package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
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
    /*
    private static final String INSERT_FILM = """
            INSERT INTO films (name, description, release_date, duration, genre, mpa_rating, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String UPDATE_FILMS = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ? WHERE id = ?";

    private static final String DELETE_FILM = "DELETE FROM films WHERE id = ?";
    private static final String GET_FILMS = """
            SELECT f.ID,  f.NAME, f.DESCRIPTION, f.DURATION, f.RELEASE_DATE, g.ID AS GENRE, g.NAME AS GENERE_NAME, m.ID AS MPA_RATING, m.NAME AS MPA_NAME FROM FILMS f
            LEFT JOIN GENRES g ON f.GENRE  = g.ID
            LEFT JOIN MPAS m ON f.MPA_RATING = m.ID
            """;
    private static final String GET_LIKES = """
            SELECT film_id, user_id FROM likes
            """;
    private static final String GET_FILMS_BY_ID = GET_FILMS + " WHERE id = ?";
    */
    private static final String INSERT_LIKE = "INSERT INTO likes (film_id, user_id, created_at, updated_at) VALUES (?, ?, ?, ?)";
    private static final String DELETE_LIKE = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
    private static final String GET_LIKES = "SELECT film_id, user_id FROM likes";
    private static final String GET_POPULAR_FILMS = "SELECT f.* FROM films f LEFT JOIN likes l ON f.ID = l.FILM_ID " +
            "GROUP BY f.ID ORDER BY COUNT(l.USER_ID) DESC LIMIT ?";

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
        /*
        Map<Integer, Set<Long>> likesMap = jdbcTemplate.query(GET_LIKES, rs -> {
            Map<Integer, Set<Long>> map = new HashMap<>();
            log.info("likes:");
            while (rs.next()) {

                int filmId = rs.getInt("film_id");
                log.info("film_id:" + filmId);
                int userId = rs.getInt("user_id");
                log.info("user_id:" + userId);
                map.computeIfAbsent(filmId, k -> new HashSet<>()).add((long) userId);
            }
            return map;
        });
        // Получаем фильмы и устанавливаем лайки
        Collection<Film> films = jdbcTemplate.query(GET_FILMS, filmRowMapper);
        if (likesMap != null) {
            films.forEach(film -> film.setLikes(likesMap.getOrDefault(film.getId(), new HashSet<>())));
        }

        return films;
        */
    }

    @Override
    public Film create(Film entity) {
        Date releaseDate = Date.valueOf(entity.getReleaseDate());
        Timestamp timestampNow = Timestamp.valueOf(LocalDateTime.now());

        List<Object> params = Arrays.asList(entity.getName(),
                entity.getDescription(),
                entity.getGenreId(),
                entity.getMpaId(),
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

        Long id = keyHolder.getKeyAs(Long.class);

        if (id != null) {
            entity.setId(id);
        } else {
            throw new InternalServerException("Не удалось сохранить данные");
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
                entity.getGenreId(),
                entity.getMpaId(),
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

