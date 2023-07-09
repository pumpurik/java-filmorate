package ru.yandex.practicum.filmorate.dao.film;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film createFilm(Film film) throws ValidationException, NotFoundException;

    Film getFilmById(long id) throws NotFoundException;

    Film updateFilm(Film film) throws ValidationException, NotFoundException;

    List<Film> findAllFilms();

    List<Film> getFilmPopular(int count);
}
