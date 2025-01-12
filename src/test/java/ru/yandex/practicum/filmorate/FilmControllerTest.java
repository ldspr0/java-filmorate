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

    @BeforeEach
    void contextLoads() {
        Film film = Film.builder()
                .name("Great Catsby")
                .description("Movie about great cats and their parties")
                .duration(90)
                .releaseDate(LocalDate.of(2000, 5, 5))
                .build();

        controller.create(film);
    }

    @Test
    public void isPossibleToUpdateFilm() {
        Film newFilm = Film.builder()
                .id(1L)
                .name("Great Catsby 2")
                .description("More Action, More Cats")
                .duration(10)
                .releaseDate(LocalDate.of(2090, 5, 5))
                .build();

        controller.update(newFilm);

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
    void isNotPossibleToCreateFilmWithEmptyName() {
        Film newFilm = Film.builder()
                .id(1L)
                .description("More Action, More Cats")
                .duration(10)
                .releaseDate(LocalDate.of(2090, 5, 5))
                .build();
        Assertions.assertThrows(ValidationException.class, () -> controller.create(newFilm));
    }

    @Test
    void isNotPossibleToCreateFilmWithDescriptionMoreThan200() {
        Film moreThan200 = Film.builder()
                .id(1L)
                .name("Great Catsby 2")
                .description("Текстовое поле — элемент графического интерфейса пользователя (GUI), " +
                        "предназначенный для ввода данных пользователем. Текстовые поля стали стандартной частью каждого сайта. " +
                        "Однако, именно в них часто встречаются ошибки, связанные с юзабилити, а для эффективного тестирования")
                .duration(10)
                .releaseDate(LocalDate.of(2090, 5, 5))
                .build();
        Film exactly201 = Film.builder()
                .id(1L)
                .name("Great Catsby 2")
                .description("Текстовое поле — элемент графического интерфейса пользователя (GUI), " +
                        "предназначенный для ввода данных пользователем. Текстовые поля стали стандартной частью каждого сайта. " +
                        "Однако, именно в них часто...")
                .duration(10)
                .releaseDate(LocalDate.of(2090, 5, 5))
                .build();
        Film exactly200 = Film.builder()
                .id(1L)
                .name("Great Catsby 2")
                .description("Текстовое поле — элемент графического интерфейса пользователя (GUI), " +
                        "предназначенный для ввода данных пользователем. Текстовые поля стали стандартной частью каждого сайта. " +
                        "Однако, именно в них часто..")
                .duration(10)
                .releaseDate(LocalDate.of(2090, 5, 5))
                .build();

        Assertions.assertThrows(ValidationException.class, () -> controller.create(moreThan200));
        Assertions.assertThrows(ValidationException.class, () -> controller.create(exactly201));

        controller.create(exactly200);
        Assertions.assertEquals(2, controller.findAll().size());
    }

    @Test
    void isNotPossibleToCreateFilmWithDateReleaseEarlierThan28dec1895() {
        Film newFilm = Film.builder()
                .id(1L)
                .name("Great Catsby 2")
                .description("More Action, More Cats")
                .duration(10)
                .releaseDate(LocalDate.of(1895, 12, 27))
                .build();
        Film exactly28 = Film.builder()
                .id(1L)
                .name("Great Catsby 2")
                .description("More Action, More Cats")
                .duration(10)
                .releaseDate(LocalDate.of(1895, 12, 28))
                .build();

        Assertions.assertThrows(ValidationException.class, () -> controller.create(newFilm));

        controller.create(exactly28);
        Assertions.assertEquals(2, controller.findAll().size());
    }

    @Test
    void isNotPossibleToCreateFilmNegativeDuration() {
        Film newFilm = Film.builder()
                .id(1L)
                .name("Great Catsby 2")
                .description("More Action, More Cats")
                .duration(-10)
                .releaseDate(LocalDate.of(2090, 5, 5))
                .build();
        Assertions.assertThrows(ValidationException.class, () -> controller.create(newFilm));
    }

    @Test
    void isNotPossibleToUpdateFilmWithNull() {
        Assertions.assertThrows(NullPointerException.class, () -> controller.update(null));
    }

    @Test
    void isNotPossibleToUpdateFilmWithNoId() {
        Film newFilm = Film.builder()
                .name("Great Catsby 2")
                .description("More Action, More Cats")
                .duration(10)
                .releaseDate(LocalDate.of(2090, 5, 5))
                .build();
        Assertions.assertThrows(ValidationException.class, () -> controller.update(newFilm));
    }

    @Test
    void isNotPossibleToUpdateFilmWithEmptyName() {
        Film newFilm = Film.builder()
                .id(1L)
                .description("More Action, More Cats")
                .duration(10)
                .releaseDate(LocalDate.of(2090, 5, 5))
                .build();
        Assertions.assertThrows(ValidationException.class, () -> controller.update(newFilm));
    }

    @Test
    void isNotPossibleToUpdateFilmWithDescriptionMoreThan200() {
        Film moreThan200 = Film.builder()
                .id(1L)
                .name("Great Catsby 2")
                .description("Текстовое поле — элемент графического интерфейса пользователя (GUI), " +
                        "предназначенный для ввода данных пользователем. Текстовые поля стали стандартной частью каждого сайта. " +
                        "Однако, именно в них часто встречаются ошибки, связанные с юзабилити, а для эффективного тестирования")
                .duration(10)
                .releaseDate(LocalDate.of(2090, 5, 5))
                .build();
        Film exactly201 = Film.builder()
                .id(1L)
                .name("Great Catsby 2")
                .description("Текстовое поле — элемент графического интерфейса пользователя (GUI), " +
                        "предназначенный для ввода данных пользователем. Текстовые поля стали стандартной частью каждого сайта. " +
                        "Однако, именно в них часто...")
                .duration(10)
                .releaseDate(LocalDate.of(2090, 5, 5))
                .build();
        Film exactly200 = Film.builder()
                .id(1L)
                .name("Great Catsby 2")
                .description("Текстовое поле — элемент графического интерфейса пользователя (GUI), " +
                        "предназначенный для ввода данных пользователем. Текстовые поля стали стандартной частью каждого сайта. " +
                        "Однако, именно в них часто..")
                .duration(10)
                .releaseDate(LocalDate.of(2090, 5, 5))
                .build();

        Assertions.assertThrows(ValidationException.class, () -> controller.update(moreThan200));
        Assertions.assertThrows(ValidationException.class, () -> controller.update(exactly201));

        controller.update(exactly200);

        Optional<Film> updatedFilmOpt = controller.findAll().stream().findFirst();
        if (updatedFilmOpt.isEmpty()) {
            Assertions.fail("Фильм не найден.");
        }

        Film updatedFilm = updatedFilmOpt.get();
        Assertions.assertEquals(1, updatedFilm.getId(), "Проверка id");
        Assertions.assertEquals("Great Catsby 2", updatedFilm.getName(), "Проверка name");
        Assertions.assertEquals("Текстовое поле — элемент графического интерфейса пользователя (GUI), " +
                "предназначенный для ввода данных пользователем. Текстовые поля стали стандартной частью каждого сайта. " +
                "Однако, именно в них часто..", updatedFilm.getDescription(), "Проверка description");
        Assertions.assertEquals(10, updatedFilm.getDuration(), "Проверка duration");
        Assertions.assertEquals(LocalDate.of(2090, 5, 5), updatedFilm.getReleaseDate(), "Проверка releaseDate");


    }

    @Test
    void isNotPossibleToUpdateFilmWithDateReleaseEarlierThan28dec1895() {
        Film newFilm = Film.builder()
                .id(1L)
                .name("Great Catsby 2")
                .description("More Action, More Cats")
                .duration(10)
                .releaseDate(LocalDate.of(1895, 12, 27))
                .build();
        Film exactly28 = Film.builder()
                .id(1L)
                .name("Great Catsby 2")
                .description("More Action, More Cats")
                .duration(10)
                .releaseDate(LocalDate.of(1895, 12, 28))
                .build();

        Assertions.assertThrows(ValidationException.class, () -> controller.update(newFilm));

        controller.update(exactly28);

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

    @Test
    void isNotPossibleToUpdateFilmNegativeDuration() {
        Film newFilm = Film.builder()
                .id(1L)
                .name("Great Catsby 2")
                .description("More Action, More Cats")
                .duration(-10)
                .releaseDate(LocalDate.of(2090, 5, 5))
                .build();
        Assertions.assertThrows(ValidationException.class, () -> controller.update(newFilm));
    }
}
