package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Set;

@RestController
@Slf4j
@RequestMapping
public class UserController {
    private UserStorage userStorage;
    private UserService userService;
    private static final String URL = "/users";

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @PostMapping(URL)
    public User createUser(@RequestBody User user) throws ValidationException {
        return userStorage.createUser(user);
    }

    @GetMapping(URL + "/{id}")
    public User getUser(@PathVariable long id) throws NotFoundException {
        return userStorage.getUser(id);
    }

    @PutMapping(URL)
    public User updateUser(@RequestBody User user) throws ValidationException, NotFoundException {
        return userStorage.updateUser(user);
    }

    @GetMapping(URL)
    public List<User> findAllUsers() throws NotFoundException {
        return userStorage.findAllUsers();
    }

    @PutMapping(URL + "/{id}/friends/{friendId}")
    public User addFriend(@PathVariable long id, @PathVariable Long friendId) throws NotFoundException {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping(URL + "/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable long id, @PathVariable Long friendId) {
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping(URL + "/{id}/friends")
    public Set<User> getAllFriends(@PathVariable long id) {
        return userService.getAllFriends(id);
    }

    @GetMapping(URL + "/{id}/friends/common/{otherId}")
    public Set<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        return userService.getCommonFriends(id, otherId);
    }
}
