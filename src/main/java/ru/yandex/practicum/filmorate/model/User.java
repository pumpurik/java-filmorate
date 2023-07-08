package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;


@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    long id;
    String email;
    String login;
    String name;
    String birthday;
    Set<Long> friends;
}
