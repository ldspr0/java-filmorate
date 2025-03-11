package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class, FilmDbStorage.class, MpaDbStorage.class, GenreDbStorage.class})
class FilmorateApplicationTests {
    private final UserDbStorage userStorage;
    private final MpaDbStorage mpaDbStorage;
    private final GenreDbStorage genreDbStorage;
    private final FilmDbStorage filmStorage;

    @BeforeEach
    void contextLoads() {


        User user = User.builder()
                .name("Alex Vonavi")
                .login("AI2.0")
                .email("test@test.test")
                .birthday(LocalDate.of(2000, 5, 5))
                .build();

        userStorage.create(user);

        User user2 = User.builder()
                .name("Giovanni Giorgio")
                .login("GG")
                .email("GG@test.test")
                .birthday(LocalDate.of(1970, 5, 5))
                .build();

        userStorage.create(user);

        User user3 = User.builder()
                .name("Common Friend")
                .login("CF")
                .email("CF@test.test")
                .birthday(LocalDate.of(2010, 5, 5))
                .build();

        userStorage.create(user);

        Mpa mpa = Mpa.builder()
                .name("G")
                .build();

        mpaDbStorage.create(mpa);

        Mpa mpa2 = Mpa.builder()
                .name("GP")
                .build();

        mpaDbStorage.create(mpa2);

        Mpa mpa3 = Mpa.builder()
                .name("R")
                .build();

        mpaDbStorage.create(mpa3);

        Genre genre = Genre.builder()
                .name("Комедия")
                .build();

        genreDbStorage.create(genre);

        Genre genre2 = Genre.builder()
                .name("Боевик")
                .build();

        genreDbStorage.create(genre2);

        Genre genre3 = Genre.builder()
                .name("Триллер")
                .build();

        genreDbStorage.create(genre3);


        Film film = Film.builder()
                .name("Great Catsby")
                .description("Movie about great cats and their parties")
                .duration(90)
                .releaseDate(LocalDate.of(2000, 5, 5))
                .mpa(mpa)
                .build();

        filmStorage.create(film);
    }

    @Test
    public void testGetUsers() {
        Collection<User> userList = userStorage.getAll();
        Assertions.assertFalse(userList.isEmpty(), "Список пользователей не должен быть пустым");
        User user = userStorage.read(userList.stream().findFirst().get().getId());
        assertThat(user).hasFieldOrPropertyWithValue("name", "Alex Vonavi");
    }

    @Test
    public void testUpdateUser() {
        Collection<User> userList = userStorage.getAll();
        Assertions.assertFalse(userList.isEmpty(), "Список пользователей не должен быть пустым");
        User user = userList.stream().findFirst().get();
        user.setName("Vonavi Alex");
        User updatedUser = userStorage.update(user);
        assertThat(updatedUser).hasFieldOrPropertyWithValue("name", "Vonavi Alex");
    }

    @Test
    public void testAddRemoveFriend() {
        Collection<User> userList = userStorage.getAll();
        Assertions.assertFalse(userList.isEmpty(), "Список пользователей не должен быть пустым");
        Assertions.assertTrue(userList.size() >= 2, "В списке должно быть как минимум два пользователя");
        List<Long> userIds = userList.stream()
                .map(User::getId)
                .toList();

        long userId1 = userIds.get(0); // ID первого пользователя
        long userId2 = userIds.get(1); // ID второго пользователя

        Assertions.assertTrue(userStorage.addFriend(userId1, userId2));
        Assertions.assertTrue(userStorage.removeFriend(userId1, userId2));

    }

    @Test
    public void testGetCommonFriends() {
        Collection<User> userList = userStorage.getAll();
        Assertions.assertFalse(userList.isEmpty(), "Список пользователей не должен быть пустым");
        Assertions.assertTrue(userList.size() >= 3, "В списке должно быть как минимум три пользователя");
        List<Long> userIds = userList.stream()
                .map(User::getId)
                .toList();

        long userId1 = userIds.get(0);
        long userId2 = userIds.get(1);
        long userId3 = userIds.get(2);

        Assertions.assertTrue(userStorage.addFriend(userId1, userId3));
        Assertions.assertTrue(userStorage.addFriend(userId2, userId3));

        Assertions.assertFalse(userStorage.getFriendList(userId1).isEmpty(), "В списке должен быть хотя бы один друг");
        Assertions.assertFalse(userStorage.getCommonFriends(userId1, userId2).isEmpty(), "В списке должен быть хотя бы один общий друг");
    }

    @Test
    public void testGetAllMpa() {
        Collection<Mpa> mpaList = mpaDbStorage.getAll();
        Assertions.assertFalse(mpaList.isEmpty(), "Список не должен быть пустым");
        Mpa mpa = mpaDbStorage.read(mpaList.stream().findFirst().get().getId());
        assertThat(mpa).hasFieldOrPropertyWithValue("name", "G");
    }

    @Test
    public void testGetAllGenres() {
        Collection<Genre> genreList = genreDbStorage.getAll();
        Assertions.assertFalse(genreList.isEmpty(), "Список не должен быть пустым");
        Genre genre = genreDbStorage.read(genreList.stream().findFirst().get().getId());
        assertThat(genre).hasFieldOrPropertyWithValue("name", "Комедия");
    }

    @Test
    public void testGetAllFilms() {
        Collection<Film> filmList = filmStorage.getAll();
        Assertions.assertFalse(filmList.isEmpty(), "Список фильмов не должен быть пустым");
        Film film = filmStorage.read(filmList.stream().findFirst().get().getId());
        assertThat(film).hasFieldOrPropertyWithValue("name", "Great Catsby");
    }

    @Test
    public void testFilmUpdate() {
        Collection<Film> filmList = filmStorage.getAll();
        Assertions.assertFalse(filmList.isEmpty(), "Список фильмов не должен быть пустым");
        Film film = filmList.stream().findFirst().get();
        film.setName("Great Catsby 2");
        Film updatedFilm = filmStorage.update(film);
        assertThat(updatedFilm).hasFieldOrPropertyWithValue("name", "Great Catsby 2");
    }

    @Test
    public void testAddLike() {
        Collection<User> userList = userStorage.getAll();
        Collection<Film> filmList = filmStorage.getAll();
        Assertions.assertFalse(userList.isEmpty(), "Список пользователей не должен быть пустым");
        Assertions.assertFalse(filmList.isEmpty(), "Список фильмов не должен быть пустым");

        Assertions.assertTrue(filmStorage.addLike(filmList.stream().findFirst().get().getId(), userList.stream().findFirst().get().getId()));

        Collection<Film> popularFilmList = filmStorage.getPopularFilms(10);
        Assertions.assertFalse(popularFilmList.isEmpty(), "Список фильмов не должен быть пустым");

        Assertions.assertTrue(filmStorage.removeLike(filmList.stream().findFirst().get().getId(), userList.stream().findFirst().get().getId()));
    }
}