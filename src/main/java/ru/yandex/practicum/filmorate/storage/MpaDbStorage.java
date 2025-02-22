package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.rowmapper.MpaRowMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static ru.yandex.practicum.filmorate.constants.SqlConstants.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MpaDbStorage implements Storage<Mpa> {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Mpa> mpaRowMapper = new MpaRowMapper();

    @Override
    public Collection<Mpa> getAll() {
        return jdbcTemplate.query(GET_MPAS + ADD_LIMIT, mpaRowMapper);
    }

    @Override
    public Mpa create(Mpa entity) {
        Timestamp timestampNow = Timestamp.valueOf(LocalDateTime.now());
        List<Object> params = Arrays.asList(entity.getName(),
                timestampNow,
                timestampNow
        );
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(INSERT_MPAS, Statement.RETURN_GENERATED_KEYS);
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
    public Mpa read(long id) {
        try {
            return jdbcTemplate.queryForObject(GET_MPAS_BY_ID + ADD_LIMIT, mpaRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Жанр с таким Id не был найден");
        }

    }

    @Override
    public Mpa update(Mpa entity) {
        Timestamp timestampNow = Timestamp.valueOf(LocalDateTime.now());
        List<Object> params = Arrays.asList(entity.getName(),
                timestampNow,
                entity.getId()
        );
        int rowsUpdated = jdbcTemplate.update(UPDATE_MPAS, params.toArray());
        if (rowsUpdated == 0) {
            throw new InternalServerException("Не удалось обновить данные");
        }
        return entity;
    }

    @Override
    public boolean delete(long id) {
        return jdbcTemplate.update(DELETE_MPAS, id) > 0;
    }
}
