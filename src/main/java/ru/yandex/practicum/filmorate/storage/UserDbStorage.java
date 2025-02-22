package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.rowmapper.UserRowMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static ru.yandex.practicum.filmorate.constants.SqlConstants.*;

@Slf4j
@Repository
@RequiredArgsConstructor
@Primary
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<User> userRowMapper = new UserRowMapper();

    @Override
    public boolean addFriend(long userId, long friendId) {
        return false;
    }

    @Override
    public boolean removeFriend(long userId, long friendId) {
        return false;
    }

    @Override
    public Collection<User> getFriendList(long userId) {
        return List.of();
    }

    @Override
    public Collection<User> getCommonFriends(long userId, long secondUserId) {
        return List.of();
    }

    @Override
    public Collection<User> getAll() {
        return jdbcTemplate.query(GET_USERS + ADD_LIMIT, userRowMapper);
    }

    @Override
    public User create(User entity) {
        Date birthday = Date.valueOf(entity.getBirthday());
        Timestamp timestampNow = Timestamp.valueOf(LocalDateTime.now());

        List<Object> params = Arrays.asList(entity.getName(),
                entity.getLogin(),
                entity.getEmail(),
                birthday,
                timestampNow,
                timestampNow
        );
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(INSERT_USERS, Statement.RETURN_GENERATED_KEYS);
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
    public User read(long id) {
        try {
            return jdbcTemplate.queryForObject(GET_USERS_BY_ID + ADD_LIMIT, userRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Жанр с таким Id не был найден");
        }
    }

    @Override
    public User update(User entity) {
        Date birthday = Date.valueOf(entity.getBirthday());
        Timestamp updatedAt = Timestamp.valueOf(LocalDateTime.now());

        List<Object> params = Arrays.asList(entity.getName(),
                entity.getLogin(),
                entity.getEmail(),
                birthday,
                updatedAt,
                entity.getId()
        );
        int rowsUpdated = jdbcTemplate.update(UPDATE_USERS, params.toArray());
        if (rowsUpdated == 0) {
            throw new InternalServerException("Не удалось обновить данные");
        }
        return entity;
    }

    @Override
    public boolean delete(long id) {
        return jdbcTemplate.update(DELETE_USERS, id) > 0;
    }
}
