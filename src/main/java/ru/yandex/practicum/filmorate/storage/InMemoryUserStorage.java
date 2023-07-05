package ru.yandex.practicum.filmorate.storage;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@Slf4j
@Data
public class InMemoryUserStorage implements UserStorage {
    private Map<Long, User> users = new HashMap<>();
    private long id = 0;

    @Override
    public void updateUsers(User user) {
        users.replace(user.getId(), user);
    }

    @Override
    public User createUser(User user) throws ValidationException {
        validateBirthday(user);
        validateLogin(user);
        validateName(user);
        validateEmail(user);
        user.setId(++id);
        user.setFriends(new LinkedHashSet<>());
        users.put(user.getId(), user);
        log.info("Пользователь добавлен: {}", users.get(user.getId()));
        return users.get(user.getId());
    }

    @Override
    public User getUser(long id) throws NotFoundException {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new NotFoundException("Пользователя с id " + id + " нет в списке");
        }
    }

    @Override
    public User updateUser(User user) throws ValidationException, NotFoundException {
        validateBirthday(user);
        validateLogin(user);
        validateName(user);
        validateEmail(user);
        if (users.containsKey(user.getId())) {
            if (user.getFriends() == null) {
                user.setFriends(new LinkedHashSet<>());
            }
            users.replace(user.getId(), user);
            log.info("Пользователь обновлен: {}", users.get(user.getId()));
            return users.get(user.getId());
        } else {
            log.info("Такого пользователя нет в списке: {}", user.getId());
            throw new NotFoundException("Пользователя с id " + user.getId() + " нет в списке");
        }
    }

    @Override
    public List<User> findAllUsers() {
        if (users.size() == 0) {
            log.info("Cписок пользователей пуст");
            return Collections.emptyList();
        } else {
            log.info("Текущее кол-во пользователей: {}", users.size());
            return new ArrayList<>(users.values());
        }
    }

    private void validateEmail(User user) throws ValidationException {
        if (user.getEmail().isBlank() || user.getEmail() == null || !user.getEmail().contains("@")) {
            log.info("Ошибка почты пользователя");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
    }

    private void validateLogin(User user) throws ValidationException {
        if (user.getLogin().isBlank() || user.getLogin() == null || user.getLogin().contains(" ")) {
            log.info("Ошибка логина");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
    }

    private void validateName(User user) throws ValidationException {
        if ((user.getName() == null || user.getName().isBlank()) && user.getLogin() != null && !user.getLogin().isBlank()) {
            user.setName(user.getLogin());
            log.info("Ошибка имени пользователя, поменяли на логин");
        } else if ((user.getName() == null || user.getName().isBlank()) && (user.getLogin() == null || user.getLogin().isBlank())) {
            log.info("Ошибка имени пользователя, на логин не поменяли");
            throw new ValidationException("Имя для отображения может быть пустым");
        }
    }

    private void validateBirthday(User user) throws ValidationException {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthdayDate = LocalDate.parse(user.getBirthday(), formatter);
        if (birthdayDate.isAfter(date)) {
            log.info("Ошибка даты рождения пользователя");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }

}
