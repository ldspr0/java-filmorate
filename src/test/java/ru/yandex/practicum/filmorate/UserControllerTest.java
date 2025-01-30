package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SpringBootTest
class UserControllerTest {
    private final UserStorage controller = new InMemoryUserStorage();
    private User updateUser;
    private User userWithEmptyName;
    private User userWithNoId;

    @BeforeEach
    void contextLoads() {
        User user = User.builder()
                .name("Alex Vonavi")
                .login("AI2.0")
                .email("test@test.test")
                .birthday(LocalDate.of(2000, 5, 5))
                .build();

        controller.create(user);

        this.updateUser = User.builder()
                .id(1L)
                .name("Xela Ivanov")
                .login("AI2.1")
                .email("tset@tset.tset")
                .birthday(LocalDate.of(1900, 5, 5))
                .build();

        this.userWithEmptyName = User.builder()
                .id(1L)
                .login("AI2.1")
                .email("tset@tset.tset")
                .birthday(LocalDate.of(1900, 5, 5))
                .build();

        this.userWithNoId = User.builder()
                .name("Xela Ivanov")
                .login("AI2.1")
                .email("tset@tset.tset")
                .birthday(LocalDate.of(1900, 5, 5))
                .build();
    }

    @Test
    public void isPossibleToUpdateUser() {
        controller.update(updateUser);

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
    void isPossibleToCreateUserWithEmptyName() {
        controller.create(userWithEmptyName);

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
        Assertions.assertThrows(ValidationException.class, () -> controller.update(userWithNoId));
    }

    @Test
    void isPossibleToUpdateUserWithEmptyName() {
        controller.update(userWithEmptyName);

        List<User> userCreatedWithoutName = controller.findAll().stream()
                .filter(user -> user.getId() == 1L && Objects.equals(user.getLogin(), user.getName()))
                .toList();
        Assertions.assertEquals(1, userCreatedWithoutName.size());
    }
}
