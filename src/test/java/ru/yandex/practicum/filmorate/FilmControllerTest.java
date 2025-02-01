package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
class FilmControllerTest {
    private final FilmService serviceContext = new FilmService(new InMemoryFilmStorage(new InMemoryUserStorage()));;
    private Film updateFilm;
    private Film newFilmBefore28;
    private Film newFilmAt28;
    private Film filmWithNoId;

    @BeforeEach
    void contextLoads() {
        Film film = Film.builder()
                .name("Great Catsby")
                .description("Movie about great cats and their parties")
                .duration(90)
                .releaseDate(LocalDate.of(2000, 5, 5))
                .build();

        serviceContext.create(film);

        this.updateFilm = Film.builder()
                .id(1L)
                .name("Great Catsby 2")
                .description("More Action, More Cats")
                .duration(10)
                .releaseDate(LocalDate.of(2090, 5, 5))
                .build();

        this.newFilmBefore28 = Film.builder()
                .id(1L)
                .name("Great Catsby 2")
                .description("More Action, More Cats")
                .duration(10)
                .releaseDate(LocalDate.of(1895, 12, 27))
                .build();

        this.newFilmAt28 = Film.builder()
                .id(1L)
                .name("Great Catsby 2")
                .description("More Action, More Cats")
                .duration(10)
                .releaseDate(LocalDate.of(1895, 12, 28))
                .build();

        this.filmWithNoId = Film.builder()
                .name("Great Catsby 2")
                .description("More Action, More Cats")
                .duration(10)
                .releaseDate(LocalDate.of(2090, 5, 5))
                .build();
    }

    @Test
    public void isPossibleToUpdateFilm() {
        serviceContext.update(this.updateFilm);

        Optional<Film> updatedFilmOpt = serviceContext.getAll().stream().findFirst();
        if (updatedFilmOpt.isEmpty()) {
            Assertions.fail("Фильм не найден.");
        }

        Film updatedFilm = updatedFilmOpt.get();
        Assertions.assertEquals(1, updatedFilm.getId(), "Проверка id");
        Assertions.assertEquals("Great Catsby 2", updatedFilm.getName(), "Проверка name");
        Assertions.assertEquals("More Action, More Cats", updatedFilm.getDescription(), "Проверка description");
        Assertions.assertEquals(10, updatedFilm.getDuration(), "Проверка duration");
        Assertions.assertEquals(LocalDate.of(2090, 5, 5), updatedFilm.getReleaseDate(), "Проверка releaseDate");

    }

    @Test
    void isNotPossibleToCreateFilmWithNull() {
        Assertions.assertThrows(NullPointerException.class, () -> serviceContext.create(null));
    }

    @Test
    void isNotPossibleToCreateFilmWithDateReleaseEarlierThan28dec1895() {
        Assertions.assertThrows(ValidationException.class, () -> serviceContext.create(this.newFilmBefore28));

        serviceContext.create(this.newFilmAt28);
        Assertions.assertEquals(2, serviceContext.getAll().size());
    }

    @Test
    void isNotPossibleToUpdateFilmWithNull() {
        Assertions.assertThrows(NullPointerException.class, () -> serviceContext.update(null));
    }

    @Test
    void isNotPossibleToUpdateFilmWithNoId() {
        Assertions.assertThrows(ValidationException.class, () -> serviceContext.update(filmWithNoId));
    }

    @Test
    void isNotPossibleToUpdateFilmWithDateReleaseEarlierThan28dec1895() {
        Assertions.assertThrows(ValidationException.class, () -> serviceContext.update(newFilmBefore28));

        serviceContext.update(newFilmAt28);

        Optional<Film> updatedFilmOpt = serviceContext.getAll().stream().findFirst();
        if (updatedFilmOpt.isEmpty()) {
            Assertions.fail("Фильм не найден.");
        }

        Film updatedFilm = updatedFilmOpt.get();

        Assertions.assertEquals(1, updatedFilm.getId(), "Проверка id");
        Assertions.assertEquals("Great Catsby 2", updatedFilm.getName(), "Проверка name");
        Assertions.assertEquals("More Action, More Cats", updatedFilm.getDescription(), "Проверка description");
        Assertions.assertEquals(10, updatedFilm.getDuration(), "Проверка duration");
        Assertions.assertEquals(LocalDate.of(1895, 12, 28), updatedFilm.getReleaseDate(), "Проверка releaseDate");
    }

}
