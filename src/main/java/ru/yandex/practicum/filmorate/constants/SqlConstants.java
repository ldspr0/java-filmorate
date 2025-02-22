package ru.yandex.practicum.filmorate.constants;

public class SqlConstants {

    public static final String GET_GENRES = "SELECT id, name FROM genres";
    public static final String GET_GENRES_BY_ID = "SELECT id, name FROM genres WHERE id = ?";
    public static final String INSERT_GENRES = "INSERT INTO genres(name, created_at, updated_at)" +
            "VALUES (?, ?, ?)";
    public static final String UPDATE_GENRES = "UPDATE genres SET name = ?, updated_at = ? WHERE id = ?";
    public static final String DELETE_GENRES = "Delete FROM genres WHERE id = ?";

    public static final String GET_MPAS = "SELECT id, name FROM mpas";
    public static final String GET_MPAS_BY_ID = "SELECT id, name FROM mpas WHERE id = ?";
    public static final String INSERT_MPAS = "INSERT INTO mpas(name, created_at, updated_at)" +
            "VALUES (?, ?, ?)";
    public static final String UPDATE_MPAS = "UPDATE mpas SET name = ?, updated_at = ? WHERE id = ?";
    public static final String DELETE_MPAS = "Delete FROM mpas WHERE id = ?";

    public static final String GET_USERS = "SELECT id, name, login, email, birthday FROM users";
    public static final String GET_USERS_BY_ID = "SELECT id, name, login, email, birthday FROM users WHERE id = ?";
    public static final String INSERT_USERS = "INSERT INTO users(name, login, email, birthday, created_at, updated_at)" +
            "VALUES (?, ?, ?, ?, ?, ?)";
    public static final String UPDATE_USERS = "UPDATE users SET name = ?, login = ?, email = ?, birthday = ?, updated_at = ? WHERE id = ?";
    public static final String DELETE_USERS = "Delete FROM users WHERE id = ?";

    public static final String GET_FILMS = "SELECT id, name, description, genre, mpa_rating, duration, release_date FROM films";
    public static final String GET_FILMS_BY_ID = "SELECT id, name, description, genre, mpa_rating, duration, release_date FROM films WHERE id = ?";
    public static final String INSERT_FILMS = "INSERT INTO films(name, description, genre, mpa_rating, duration, release_date, created_at, updated_at)" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String UPDATE_FILMS = "UPDATE films SET name = ?, description = ?, genre = ?, mpa_rating = ?, duration = ?, release_date = ?, updated_at = ? WHERE id = ?";
    public static final String DELETE_FILMS = "Delete FROM films WHERE id = ?";

    public static final String ADD_LIMIT = " LIMIT 1000 ";
}
