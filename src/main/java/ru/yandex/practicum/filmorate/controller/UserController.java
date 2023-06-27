package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@RequestBody User user) throws ValidationException {
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) throws ValidationException {
        return userService.updateUser(user);
    }

    @GetMapping
    public List<User> findAllUsers() throws ValidationException {
        return userService.findAllUsers();
    }
}
