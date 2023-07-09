package ru.yandex.practicum.filmorate.dao.likes;


import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

public interface LikesStorage {
    public void addLike(Film film, User user);

    public void removeLike(Film film, User user);

}
