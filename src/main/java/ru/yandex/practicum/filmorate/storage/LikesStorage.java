package ru.yandex.practicum.filmorate.storage;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Data
public class LikesStorage {
    private Map<Film, User> likes = new LinkedHashMap<>();
}
