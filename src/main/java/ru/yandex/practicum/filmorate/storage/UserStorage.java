package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {
    Map<Long, User> getUsers();

    void updateUsers(User user);

    User createUser(User user) throws ValidationException;

    User getUser(long id) throws NotFoundException;

    User updateUser(User user) throws ValidationException, NotFoundException;

    List<User> findAllUsers();
}
