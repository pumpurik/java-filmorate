package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
     Film createFilm(Film film) throws ValidationException;
     Film updateFilm(Film film) throws ValidationException;
     List<Film> findAllFilms() throws ValidationException;
}
