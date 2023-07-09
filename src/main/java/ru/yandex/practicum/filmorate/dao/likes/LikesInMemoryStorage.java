package ru.yandex.practicum.filmorate.dao.likes;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.LinkedHashMap;
import java.util.Map;

@Component("likesInMemoryStorage")
@Data
@Slf4j
public class LikesInMemoryStorage implements LikesStorage {
    private Map<Film, User> likes = new LinkedHashMap<>();

    @Override
    public void addLike(Film film, User user) {
        likes.put(film, user);
    }

    @Override
    public void removeLike(Film film, User user) {
        if (likes.containsKey(film)) {
            likes.remove(film, user);
        }
    }
}
