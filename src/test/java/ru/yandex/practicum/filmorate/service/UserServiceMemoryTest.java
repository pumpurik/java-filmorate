package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
public class UserServiceMemoryTest {
    @InjectMocks
    private UserServiceMemory userService;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Test
    void createUserValid() throws ValidationException {
        LocalDate now = LocalDate.now();
        User user = new User(1, "pomogite@", "login", "name123", now.format(formatter));
        User user1 = userService.createUser(user);
        assertEquals(user1,user);
    }
    @Test
    void createUserWithoutName() {
        LocalDate now = LocalDate.now();
        User user = new User(1, "pomogite@", "login", "", now.format(formatter));
        ValidationException validationException = assertThrows(ValidationException.class, () -> {
            userService.createUser(user);
        });
        assertEquals("имя для отображения может быть пустым— в таком случае будет использован " +
                "логин", validationException.getMessage());
        assertEquals("login", user.getName());
    }
    @Test
    void createUserWithEmptyEmail() {
        LocalDate now = LocalDate.now();
        User user = new User(2, "", "login", "user2", now.format(formatter));
        ValidationException validationException = assertThrows(ValidationException.class, () -> {
            userService.createUser(user);
        });
        assertEquals("электронная почта не может быть пустой и должна содержать символ @", validationException.getMessage());
    }
    @Test
    void createUserWithEmailWithoutSobaka(){
        LocalDate now = LocalDate.now();
        User user1 = new User(1, "pomogite", "login", "user1", now.format(formatter));
        ValidationException validationException = assertThrows(ValidationException.class, () -> {
            userService.createUser(user1);
        });
        assertEquals("электронная почта не может быть пустой и должна содержать символ @", validationException.getMessage());
    }
    @Test
    void createUserWithEmptyLogin(){
        LocalDate now = LocalDate.now();
        User user = new User(1, "pomogite@", "", "user", now.format(formatter));
        ValidationException validationException = assertThrows(ValidationException.class, () -> {
            userService.createUser(user);
        });
        assertEquals("логин не может быть пустым и содержать пробелы", validationException.getMessage());
    }
    @Test
    void createUserWithLoginSpace(){
        LocalDate now = LocalDate.now();
        User user = new User(1, "pomogite@", "d o r i m e", "user", now.format(formatter));
        ValidationException validationException = assertThrows(ValidationException.class, () -> {
            userService.createUser(user);
        });
        assertEquals("логин не может быть пустым и содержать пробелы", validationException.getMessage());
    }
    @Test
    void createUserNotValidBirthday(){
        LocalDate future = LocalDate.now().plusMonths(1);;
        User user = new User(1, "pomogite@", "", "user1", future.format(formatter));
        ValidationException validationException = assertThrows(ValidationException.class, () -> {
            userService.createUser(user);
        });
        assertEquals("дата рождения не может быть в будущем", validationException.getMessage());
    }

    @Test
    void findAllUsers() throws ValidationException {
        LocalDate now = LocalDate.now();
        User user1 = new User(1, "user1@", "user1", "user1", now.format(formatter));
        User user2 = new User(1, "user2@", "user2", "user2", now.format(formatter));
        userService.createUser(user1);
        userService.createUser(user2);
        List<User> testUsers = new ArrayList(Arrays.asList(user1,user2));
        assertEquals(userService.findAllUsers(),testUsers);
    }
    @Test
    void findAllUsersNotValid() throws ValidationException {
        ValidationException validationException = assertThrows(ValidationException.class, () -> {
            userService.findAllUsers();
        });
        assertEquals("Список пользователей пуст",validationException.getMessage());
    }
    @Test
    void updateUserValid() throws ValidationException {
        LocalDate now = LocalDate.now();
        User createUser = new User(1, "user1@", "user1", "user1", now.format(formatter));
        User updateUser = new User(1, "user2@", "user2", "user2", now.format(formatter));
        userService.createUser(createUser);
        User user = userService.updateUser(updateUser);
        assertEquals(user,updateUser);
    }
    @Test
    void updateUserNotValid() throws ValidationException {
        LocalDate now = LocalDate.now();
        User createUser = new User(1, "user1@", "user1", "user1", now.format(formatter));
        User updateUser = new User(100, "user2@", "user2", "user2", now.format(formatter));
        userService.createUser(createUser);
        ValidationException validationException = assertThrows(ValidationException.class, () -> {
        User user = userService.updateUser(updateUser);
        });
        assertEquals("Такого пользователя не существует",validationException.getMessage());
    }
}
