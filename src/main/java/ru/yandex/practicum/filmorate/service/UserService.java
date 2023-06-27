package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    User createUser(User user) throws ValidationException;

    User updateUser(User user) throws ValidationException;

    List<User> findAllUsers() throws ValidationException;
}
