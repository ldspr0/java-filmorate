package ru.yandex.practicum.filmorate.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = Film.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .build();

        // Маппинг Mpa
        Mpa mpa = Mpa.builder()
                .id(rs.getLong("mpa_id"))
                .name(rs.getString("mpa_name"))
                .build();
        film.setMpa(mpa);

        // Маппинг Genres
        String genresString = rs.getString("genres");
        Set<Genre> genres = new HashSet<>();

        if (genresString != null && !genresString.isEmpty()) {
            genres = Arrays.stream(genresString.split(", "))
                    .map(genreEntry -> {
                        String[] parts = genreEntry.split(": ");
                        if (parts.length == 2) {
                            long id = Integer.parseInt(parts[0]);
                            String name = parts[1];
                            return Genre.builder()
                                    .id(id)
                                    .name(name)
                                    .build();
                        } else {
                            throw new IllegalArgumentException("Некорректный формат жанра: " + genreEntry);
                        }
                    })
                    .collect(Collectors.toSet());
        }

        film.setGenres(genres);

        // Маппинг Likes
        String likesString = rs.getString("likes");
        Set<Long> likes = new HashSet<>();

        if (likesString != null && !likesString.isEmpty()) {
            likes = Arrays.stream(likesString.split(", "))
                    .map(Long::parseLong)
                    .collect(Collectors.toSet());
        }

        film.setLikes(likes);

        return film;
    }
}
