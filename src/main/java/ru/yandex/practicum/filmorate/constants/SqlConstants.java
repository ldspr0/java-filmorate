package ru.yandex.practicum.filmorate.constants;

public class SqlConstants {

    public static final String GET_GENRES = "SELECT id, name FROM genres";
    public static final String GET_GENRES_BY_ID = "SELECT id, name FROM genres WHERE id = ?";
    public static final String INSERT_GENRES = "INSERT INTO genres(name, created_at, updated_at)" +
            "VALUES (?, ?, ?) returning id";
    public static final String UPDATE_GENRES = "UPDATE genres SET name = ?, updated_at = ? WHERE id = ?";
    public static final String DELETE_GENRES = "Delete FROM genres WHERE id = ?";

    public static final String GET_MPAS = "SELECT id, name FROM mpas";
    public static final String GET_MPAS_BY_ID = "SELECT id, name FROM mpas WHERE id = ?";
    public static final String INSERT_MPAS = "INSERT INTO mpas(name, created_at, updated_at)" +
            "VALUES (?, ?, ?) returning id";
    public static final String UPDATE_MPAS = "UPDATE mpas SET name = ?, updated_at = ? WHERE id = ?";
    public static final String DELETE_MPAS = "Delete FROM mpas WHERE id = ?";

    public static final String ADD_LIMIT = " LIMIT 1000 ";
}
