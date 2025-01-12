package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SpringBootTest
class UserControllerTest {
    private final UserController controller = new UserController();

    @BeforeEach
    void contextLoads() {
        User user = User.builder()
                .name("Alex Vonavi")
                .login("AI2.0")
                .email("test@test.test")
                .birthday(LocalDate.of(2000, 5, 5))
                .build();

        controller.create(user);
    }

    @Test
    public void isPossibleToUpdateUser() {
        User newUser = User.builder()
                .id(1L)
                .name("Xela Ivanov")
                .login("AI2.1")
                .email("tset@tset.tset")
                .birthday(LocalDate.of(1900, 5, 5))
                .build();

        controller.update(newUser);

        Optional<User> updatedUserOpt = controller.findAll().stream().findFirst();
        if (updatedUserOpt.isEmpty()) {
            Assertions.fail("Юзер не найден.");
        }

        User updatedUser = updatedUserOpt.get();
        Assertions.assertEquals(1, updatedUser.getId(), "Проверка id");
        Assertions.assertEquals("Xela Ivanov", updatedUser.getName(), "Проверка name");
        Assertions.assertEquals("AI2.1", updatedUser.getLogin(), "Проверка login");
        Assertions.assertEquals("tset@tset.tset", updatedUser.getEmail(), "Проверка email");
        Assertions.assertEquals(LocalDate.of(1900, 5, 5), updatedUser.getBirthday(), "Проверка birthday");

    }

    @Test
    void isNotPossibleToCreateUserWithNull() {
        Assertions.assertThrows(NullPointerException.class, () -> controller.create(null));
    }

    @Test
    void isNotPossibleToCreateUserWithIncorrectEmail() {
        User newUser = User.builder()
                .id(1L)
                .name("Xela Ivanov")
                .login("AI2.1")
                .email("tset.tsettset")
                .birthday(LocalDate.of(1900, 5, 5))
                .build();
        User newUserWithEmpty = User.builder()
                .id(1L)
                .name("Xela Ivanov")
                .login("AI2.1")
                .birthday(LocalDate.of(1900, 5, 5))
                .build();

        Assertions.assertThrows(ValidationException.class, () -> controller.create(newUser));
        Assertions.assertThrows(ValidationException.class, () -> controller.create(newUserWithEmpty));
    }

    @Test
    void isNotPossibleToCreateUserWithSpacesInLoginOrWithoutLogin() {
        User newUser = User.builder()
                .id(1L)
                .name("Xela Ivanov")
                .email("tset@tset.tset")
                .birthday(LocalDate.of(1900, 5, 5))
                .build();
        User newUserWithSpaces = User.builder()
                .id(1L)
                .name("Xela Ivanov")
                .login("AI 2.1")
                .email("tset@tset.tset")
                .birthday(LocalDate.of(1900, 5, 5))
                .build();

        Assertions.assertThrows(ValidationException.class, () -> controller.create(newUser));
        Assertions.assertThrows(ValidationException.class, () -> controller.create(newUserWithSpaces));
    }

    @Test
    void isNotPossibleToCreateUserWithBirthdayInFuture() {
        User newUser = User.builder()
                .id(1L)
                .name("Xela Ivanov")
                .login("AI2.1")
                .email("tset@tset.tset")
                .birthday(LocalDate.now().plusDays(1L))
                .build();
        User newUserWithout = User.builder()
                .id(1L)
                .name("Xela Ivanov")
                .login("AI2.1")
                .email("tset@tset.tset")
                .build();
        User newUserToday = User.builder()
                .id(1L)
                .name("Xela Ivanov")
                .login("AI2.1")
                .email("tset@tset.tset")
                .birthday(LocalDate.now())
                .build();

        Assertions.assertThrows(ValidationException.class, () -> controller.create(newUser));

        controller.create(newUserWithout);
        Assertions.assertEquals(2, controller.findAll().size());

        controller.create(newUserToday);
        Assertions.assertEquals(3, controller.findAll().size());
    }

    @Test
    void isPossibleToCreateUserWithEmptyName() {
        User newUser = User.builder()
                .id(2L)
                .login("AI2.1")
                .email("tset@tset.tset")
                .birthday(LocalDate.of(1900, 5, 5))
                .build();
        controller.create(newUser);

        List<User> userCreatedWithoutName = controller.findAll().stream()
                .filter(user -> user.getId() == 2L && Objects.equals(user.getLogin(), user.getName()))
                .toList();
        Assertions.assertEquals(1, userCreatedWithoutName.size());

    }

    @Test
    void isNotPossibleToUpdateUserWithNull() {
        Assertions.assertThrows(NullPointerException.class, () -> controller.update(null));
    }

    @Test
    void isNotPossibleToUPdateUserWithNoId() {
        User newUser = User.builder()
                .name("Xela Ivanov")
                .login("AI2.1")
                .email("tset@tset.tset")
                .birthday(LocalDate.of(1900, 5, 5))
                .build();
        Assertions.assertThrows(ValidationException.class, () -> controller.update(newUser));
    }

    @Test
    void isNotPossibleToUpdateUserWithIncorrectEmail() {
        User newUser = User.builder()
                .id(1L)
                .name("Xela Ivanov")
                .login("AI2.1")
                .email("tset.tsettset")
                .birthday(LocalDate.of(1900, 5, 5))
                .build();
        User newUserWithEmpty = User.builder()
                .id(1L)
                .name("Xela Ivanov")
                .login("AI2.1")
                .birthday(LocalDate.of(1900, 5, 5))
                .build();

        Assertions.assertThrows(ValidationException.class, () -> controller.update(newUser));
        Assertions.assertThrows(ValidationException.class, () -> controller.update(newUserWithEmpty));
    }

    @Test
    void isNotPossibleToUpdateUserWithSpacesInLoginOrWithoutLogin() {
        User newUser = User.builder()
                .id(1L)
                .name("Xela Ivanov")
                .email("tset@tset.tset")
                .birthday(LocalDate.of(1900, 5, 5))
                .build();
        User newUserWithSpaces = User.builder()
                .id(1L)
                .name("Xela Ivanov")
                .login("AI 2.1")
                .email("tset@tset.tset")
                .birthday(LocalDate.of(1900, 5, 5))
                .build();

        Assertions.assertThrows(ValidationException.class, () -> controller.update(newUser));
        Assertions.assertThrows(ValidationException.class, () -> controller.update(newUserWithSpaces));
    }

    @Test
    void isNotPossibleToUpdateUserWithBirthdayInFuture() {
        User newUser = User.builder()
                .id(1L)
                .name("Xela Ivanov")
                .login("AI2.1")
                .email("tset@tset.tset")
                .birthday(LocalDate.now().plusDays(1L))
                .build();
        User newUserWithout = User.builder()
                .id(1L)
                .name("Xela Ivanov")
                .login("AI2.1")
                .email("tset@tset.tset")
                .build();
        User newUserToday = User.builder()
                .id(1L)
                .name("Xela Ivanov")
                .login("AI2.1")
                .email("tset@tset.tset")
                .birthday(LocalDate.now())
                .build();

        Assertions.assertThrows(ValidationException.class, () -> controller.update(newUser));
        Assertions.assertDoesNotThrow(() -> controller.update(newUserWithout));

        controller.update(newUserToday);
        Optional<User> updatedUserOpt = controller.findAll().stream().findFirst();
        if (updatedUserOpt.isEmpty()) {
            Assertions.fail("Юзер не найден.");
        }

        User updatedUser = updatedUserOpt.get();
        Assertions.assertEquals(1, updatedUser.getId(), "Проверка id");
        Assertions.assertEquals(LocalDate.now(), updatedUser.getBirthday(), "Проверка birthday");
    }

    @Test
    void isPossibleToUpdateUserWithEmptyName() {
        User newUser = User.builder()
                .id(1L)
                .login("AI2.1")
                .email("tset@tset.tset")
                .birthday(LocalDate.of(1900, 5, 5))
                .build();
        controller.update(newUser);

        List<User> userCreatedWithoutName = controller.findAll().stream()
                .filter(user -> user.getId() == 1L && Objects.equals(user.getLogin(), user.getName()))
                .toList();
        Assertions.assertEquals(1, userCreatedWithoutName.size());
    }
}
