package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserServiceMemory implements UserService {
    private Map<Integer, User> users = new HashMap<>();
    int id = 0;

    @Override
    public User createUser(User user) throws ValidationException {
        validateBirthday(user);
        validateLogin(user);
        validateName(user);
        validateEmail(user);
        user.setId(++id);
        users.put(user.getId(), user);
        log.info("Пользователь добавлен: {}", users.get(user.getId()));
        return users.get(user.getId());
    }

    @Override
    public User updateUser(User user) throws ValidationException {
        validateBirthday(user);
        validateLogin(user);
        validateName(user);
        validateEmail(user);
        if (users.containsKey(user.getId())) {
            users.replace(user.getId(), user);
            log.info("Пользователь обновлен: {}", users.get(user.getId()));
            return users.get(user.getId());
        } else {
            log.info("Такого пользователя нет в списке");
            throw new ValidationException("Такого пользователя не существует");
        }
    }

    @Override
    public List<User> findAllUsers() throws ValidationException {
        if (users.size() == 0) {
            log.info("Cписок пользователей пуст");
            throw new ValidationException("Список пользователей пуст");
        } else {
            log.info("Текущее кол-во пользователей: {}", users.size());
            return new ArrayList<>(users.values());
        }
    }

    private void validateEmail(User user) throws ValidationException {
        if (user.getEmail().isBlank() || user.getEmail() == null || !user.getEmail().contains("@")) {
            log.info("Ошибка почты пользователя");
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        }
    }

    private void validateLogin(User user) throws ValidationException {
        if (user.getLogin().isBlank() || user.getLogin() == null || user.getLogin().contains(" ")) {
            log.info("Ошибка логина");
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        }
    }

    private void validateName(User user) throws ValidationException {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Ошибка имени пользователя");
            throw new ValidationException("имя для отображения может быть пустым— в таком случае будет использован " +
                    "логин");
        }
    }

    private void validateBirthday(User user) throws ValidationException {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthdayDate = LocalDate.parse(user.getBirthday(), formatter);
        if (birthdayDate.isAfter(date)) {
            log.info("Ошибка даты рождения пользователя");
            throw new ValidationException("дата рождения не может быть в будущем");
        }
    }

}
