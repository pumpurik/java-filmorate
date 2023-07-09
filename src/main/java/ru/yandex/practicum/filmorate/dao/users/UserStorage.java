package ru.yandex.practicum.filmorate.dao.users;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User createUser(User user) throws ValidationException, NotFoundException;

    User getUser(long id) throws NotFoundException;

    User updateUser(User user) throws ValidationException, NotFoundException;

    List<User> findAllUsers();

    List<User> getFriendByUserId(long id);

    void removeUser(long id) throws NotFoundException;
}
