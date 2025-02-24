package ru.yandex.practicum.filmorate.constants;

public class SqlConstants {
    // Genres
    public static final String GET_GENRES = "SELECT id, name FROM genres";
    public static final String GET_GENRES_BY_ID = GET_GENRES + " WHERE id = ?";
    public static final String INSERT_GENRES = "INSERT INTO genres(name, created_at, updated_at)" +
            "VALUES (?, ?, ?)";
    public static final String UPDATE_GENRES = "UPDATE genres SET name = ?, updated_at = ? WHERE id = ?";
    public static final String DELETE_GENRES = "Delete FROM genres WHERE id = ?";

    // MPAs
    public static final String GET_MPAS = "SELECT id, name FROM mpas";
    public static final String GET_MPAS_BY_ID = GET_MPAS + " WHERE id = ?";
    public static final String INSERT_MPAS = "INSERT INTO mpas(name, created_at, updated_at)" +
            "VALUES (?, ?, ?)";
    public static final String UPDATE_MPAS = "UPDATE mpas SET name = ?, updated_at = ? WHERE id = ?";
    public static final String DELETE_MPAS = "Delete FROM mpas WHERE id = ?";

    // Users
    public static final String GET_USERS = "SELECT id, name, login, email, birthday FROM users";
    public static final String GET_USERS_BY_ID = GET_USERS + " WHERE id = ?";
    public static final String INSERT_USERS = "INSERT INTO users(name, login, email, birthday, created_at, updated_at)" +
            "VALUES (?, ?, ?, ?, ?, ?)";
    public static final String UPDATE_USERS = "UPDATE users SET name = ?, login = ?, email = ?, birthday = ?, updated_at = ? WHERE id = ?";
    public static final String DELETE_USERS = "Delete FROM users WHERE id = ?";

    // Friends
    public static final String GET_FRIENDS = "SELECT u.id, u.name, u.login, u.email, u.birthday FROM friends f LEFT JOIN users u ON f.friend_id = u.id WHERE f.user_id = ?";
    public static final String GET_COMMON_FRIENDS = """
            SELECT u.id, u.name, u.login, u.email, u.birthday
            FROM friends f1
            INNER JOIN friends f2 ON f1.friend_id = f2.friend_id
            INNER JOIN users u ON f1.friend_id = u.id
            WHERE f1.user_id = ? AND f2.user_id = ?
            """;
    public static final String INSERT_FRIENDS = "INSERT INTO friends(user_id, friend_id, status, created_at, updated_at)" +
            "VALUES (?, ?, ?, ?, ?)";
    public static final String DELETE_FRIENDS = "Delete FROM friends WHERE user_id = ? AND friend_id = ?";

    // Films
    public static final String GET_FILMS = """
            SELECT f.id, f.name, f.description, m.id AS mpa_id, m.name AS mpa_name, f.duration, f.release_date,
                (SELECT LISTAGG(g.id || ': ' || g.name, ', ') WITHIN GROUP (ORDER BY g.id)
                    FROM film_genre fg
                    INNER JOIN genres g ON g.id = fg.genre_id
                    WHERE fg.film_id = f.id
                ) AS genres
            FROM films f
            INNER JOIN mpas m ON f.mpa_rating = m.id
            """;
    public static final String GET_FILMS_BY_ID = GET_FILMS + " WHERE f.id = ?";
    public static final String GET_POPULAR_FILMS = GET_FILMS +
            " LEFT JOIN likes l ON f.ID = l.FILM_ID " +
            " GROUP BY f.ID " +
            " ORDER BY COUNT(l.USER_ID) DESC " +
            " LIMIT ? ";
    public static final String INSERT_FILMS = "INSERT INTO films(name, description, mpa_rating, duration, release_date, created_at, updated_at)" +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";
    public static final String UPDATE_FILMS = "UPDATE films SET name = ?, description = ?, mpa_rating = ?, duration = ?, release_date = ?, updated_at = ? WHERE id = ?";
    public static final String DELETE_FILMS = "Delete FROM films WHERE id = ?";

    // Film to Genres
    public static final String INSERT_FILM_GENRES = "INSERT INTO film_genre(film_id, genre_id, created_at, updated_at)" +
            "VALUES (?, ?, ?, ?)";

    // Likes
    public static final String GET_LIKES = "SELECT film_id, user_id FROM likes";
    public static final String INSERT_LIKE = "INSERT INTO likes (film_id, user_id, created_at, updated_at) VALUES (?, ?, ?, ?)";
    public static final String DELETE_LIKE = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
}
