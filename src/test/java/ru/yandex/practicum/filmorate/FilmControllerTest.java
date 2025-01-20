package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
class FilmControllerTest {
    private final FilmController controller = new FilmController();
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

        controller.create(film);

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
        controller.update(this.updateFilm);

        Optional<Film> updatedFilmOpt = controller.findAll().stream().findFirst();
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
        Assertions.assertThrows(NullPointerException.class, () -> controller.create(null));
    }

    @Test
    void isNotPossibleToCreateFilmWithDateReleaseEarlierThan28dec1895() {
        Assertions.assertThrows(ValidationException.class, () -> controller.create(this.newFilmBefore28));

        controller.create(this.newFilmAt28);
        Assertions.assertEquals(2, controller.findAll().size());
    }

    @Test
    void isNotPossibleToUpdateFilmWithNull() {
        Assertions.assertThrows(NullPointerException.class, () -> controller.update(null));
    }

    @Test
    void isNotPossibleToUpdateFilmWithNoId() {
        Assertions.assertThrows(ValidationException.class, () -> controller.update(filmWithNoId));
    }

    @Test
    void isNotPossibleToUpdateFilmWithDateReleaseEarlierThan28dec1895() {
        Assertions.assertThrows(ValidationException.class, () -> controller.update(newFilmBefore28));

        controller.update(newFilmAt28);

        Optional<Film> updatedFilmOpt = controller.findAll().stream().findFirst();
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
