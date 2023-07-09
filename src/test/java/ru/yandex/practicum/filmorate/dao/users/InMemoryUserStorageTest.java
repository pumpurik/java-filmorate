package ru.yandex.practicum.filmorate.dao.users;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
public class InMemoryUserStorageTest {
    @InjectMocks
    private InMemoryUserStorage userService;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Test
    void createUserValid() throws ValidationException {
        LocalDate now = LocalDate.now();
        User user = new User(1, "pomogite@", "login", "name123", now.format(formatter), Collections.emptySet());
        User user1 = userService.createUser(user);
        assertEquals(user1, user);
    }

    @Test
    void createUserWithoutNameWithLogin() throws ValidationException {
        LocalDate now = LocalDate.now();
        User user = new User(1, "pomogite@", "login", "", now.format(formatter), Collections.emptySet());
        userService.createUser(user);
        assertEquals("login", user.getName());
    }

    @Test
    void createUserWithEmptyEmail() {
        LocalDate now = LocalDate.now();
        User user = new User(2, "", "login", "user2", now.format(formatter), Collections.emptySet());
        ValidationException validationException = assertThrows(ValidationException.class, () -> {
            userService.createUser(user);
        });
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", validationException.getMessage());
    }

    @Test
    void createUserWithEmailWithoutSobaka() {
        LocalDate now = LocalDate.now();
        User user1 = new User(1, "pomogite", "login", "user1", now.format(formatter), Collections.emptySet());
        ValidationException validationException = assertThrows(ValidationException.class, () -> {
            userService.createUser(user1);
        });
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", validationException.getMessage());
    }

    @Test
    void createUserWithEmptyLogin() {
        LocalDate now = LocalDate.now();
        User user = new User(1, "pomogite@", "", "user", now.format(formatter), Collections.emptySet());
        ValidationException validationException = assertThrows(ValidationException.class, () -> {
            userService.createUser(user);
        });
        assertEquals("Логин не может быть пустым и содержать пробелы", validationException.getMessage());
    }

    @Test
    void createUserWithLoginSpace() {
        LocalDate now = LocalDate.now();
        User user = new User(1, "pomogite@", "d o r i m e", "user", now.format(formatter), Collections.emptySet());
        ValidationException validationException = assertThrows(ValidationException.class, () -> {
            userService.createUser(user);
        });
        assertEquals("Логин не может быть пустым и содержать пробелы", validationException.getMessage());
    }

    @Test
    void createUserNotValidBirthday() {
        LocalDate future = LocalDate.now().plusMonths(1);
        ;
        User user = new User(1, "pomogite@", "", "user1", future.format(formatter), Collections.emptySet());
        ValidationException validationException = assertThrows(ValidationException.class, () -> {
            userService.createUser(user);
        });
        assertEquals("Дата рождения не может быть в будущем", validationException.getMessage());
    }

    @Test
    void findAllUsers() throws ValidationException, NotFoundException {
        LocalDate now = LocalDate.now();
        User user1 = new User(1, "user1@", "user1", "user1", now.format(formatter), Collections.emptySet());
        User user2 = new User(1, "user2@", "user2", "user2", now.format(formatter), Collections.emptySet());
        userService.createUser(user1);
        userService.createUser(user2);
        List<User> testUsers = new ArrayList(Arrays.asList(user1, user2));
        assertEquals(userService.findAllUsers(), testUsers);
    }

    @Test
    void updateUserValid() throws ValidationException, NotFoundException {
        LocalDate now = LocalDate.now();
        User createUser = new User(1, "user1@", "user1", "user1", now.format(formatter), Collections.emptySet());
        User updateUser = new User(1, "user2@", "user2", "user2", now.format(formatter), Collections.emptySet());
        userService.createUser(createUser);
        User user = userService.updateUser(updateUser);
        assertEquals(user, updateUser);
    }

    @Test
    void updateUserNotValid() throws ValidationException, NotFoundException {
        LocalDate now = LocalDate.now();
        User createUser = new User(1, "user1@", "user1", "user1", now.format(formatter), Collections.emptySet());
        User updateUser = new User(100, "user2@", "user2", "user2", now.format(formatter), Collections.emptySet());
        userService.createUser(createUser);
        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> {
            User user = userService.updateUser(updateUser);
        });
        assertEquals("Пользователя с id " + updateUser.getId() + " нет в списке", notFoundException.getMessage());
    }
}
