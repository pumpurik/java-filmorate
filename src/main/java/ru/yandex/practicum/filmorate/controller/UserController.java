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
@RequestMapping("/users")
public class UserController {
    private UserStorage userStorage;
    private UserService userService;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@RequestBody User user) throws ValidationException {
        return userStorage.createUser(user);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable long id) throws NotFoundException {
        return userStorage.getUser(id);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) throws ValidationException, NotFoundException {
        return userStorage.updateUser(user);
    }

    @GetMapping
    public List<User> findAllUsers() throws NotFoundException {
        return userStorage.findAllUsers();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable long id, @PathVariable Long friendId) throws NotFoundException {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable long id, @PathVariable Long friendId) {
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Set<User> getAllFriends(@PathVariable long id) {
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        return userService.getCommonFriends(id, otherId);
    }
}
