package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {
    Map<Integer, Film> getFilms();

    void updateFilms(Film film);

    Film createFilm(Film film) throws ValidationException;

    Film getFilm(int id) throws NotFoundException;

    Film updateFilm(Film film) throws ValidationException, NotFoundException;

    List<Film> findAllFilms();
}
