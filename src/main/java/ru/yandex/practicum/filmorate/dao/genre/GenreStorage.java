package ru.yandex.practicum.filmorate.dao.genre;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    Genre createGenre(Genre genre) throws NotFoundException;

    List<Genre> findAllGenre();

    Genre findGenreById(long id) throws NotFoundException;

    Genre updateGenre(Genre genre) throws NotFoundException;
}
