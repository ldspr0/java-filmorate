package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.rowmapper.GenreRowMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static ru.yandex.practicum.filmorate.constants.SqlConstants.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements Storage<Genre> {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Genre> genreRowMapper = new GenreRowMapper();

    @Override
    public Collection<Genre> getAll() {
        return jdbcTemplate.query(GET_GENRES + ADD_LIMIT, genreRowMapper);
    }

    @Override
    public Genre create(Genre entity) {
        List<Object> params = Arrays.asList(entity.getName(),
                LocalDate.now(),
                LocalDate.now()
        );
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(INSERT_GENRES, Statement.RETURN_GENERATED_KEYS);
            for (int idx = 0; idx < params.size(); idx++) {
                ps.setObject(idx + 1, params.get(idx));
            }
            return ps;
        }, keyHolder);

        Integer id = keyHolder.getKeyAs(Integer.class);

        if (id != null) {
            entity.setId(id);
        } else {
            throw new InternalServerException("Не удалось сохранить данные");
        }

        return entity;
    }

    @Override
    public Genre read(long id) {
        return jdbcTemplate.queryForObject(GET_GENRES_BY_ID + ADD_LIMIT, genreRowMapper, id);
    }

    @Override
    public Genre update(Genre entity) {
        List<Object> params = Arrays.asList(entity.getName(),
                LocalDate.now(),
                entity.getId()
        );
        int rowsUpdated = jdbcTemplate.update(UPDATE_GENRES, params);
        if (rowsUpdated == 0) {
            throw new InternalServerException("Не удалось обновить данные");
        }
        return entity;
    }

    @Override
    public boolean delete(long id) {
        return jdbcTemplate.update(DELETE_GENRES, id) > 0;
    }
}
