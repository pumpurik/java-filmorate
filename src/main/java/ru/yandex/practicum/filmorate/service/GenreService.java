package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.genre.GenreStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
@AllArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;

    public Genre createGenre(Genre genre) throws NotFoundException {
        return genreStorage.createGenre(genre);
    }

    public Genre getGenre(long id) throws NotFoundException {
        return genreStorage.findGenreById(id);
    }

    public Genre updateGenre(Genre genre) throws NotFoundException {
        return genreStorage.updateGenre(genre);
    }

    public List<Genre> findAllGenres() {
        return genreStorage.findAllGenre();
    }
}
