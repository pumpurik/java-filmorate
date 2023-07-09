package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@RequestBody User user) throws ValidationException, NotFoundException {
        return userService.createUser(user);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable long id) throws NotFoundException {
        return userService.getUser(id);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) throws ValidationException, NotFoundException {
        return userService.updateUser(user);
    }

    @GetMapping
    public List<User> findAllUsers() throws NotFoundException {
        return userService.findAllUsers();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable long id, @PathVariable Long friendId) throws NotFoundException {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable long id, @PathVariable Long friendId) throws NotFoundException {
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@PathVariable long id) throws NotFoundException {
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) throws NotFoundException {
        return userService.getCommonFriends(id, otherId);
    }
}
