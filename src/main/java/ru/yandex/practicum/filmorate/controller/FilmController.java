package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping
public class FilmController {

    private FilmStorage filmStorage;
    private FilmService filmService;
    private final static String URL = "/films";

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @PostMapping(URL)
    public Film createFilm(@RequestBody Film film) throws ValidationException {
        return filmStorage.createFilm(film);
    }

    @GetMapping(URL + "/{id}")
    public Film getFilm(@PathVariable int id) throws NotFoundException {
        return filmStorage.getFilm(id);
    }

    @PutMapping(URL)
    public Film updateFilm(@RequestBody Film film) throws ValidationException, NotFoundException {

        return filmStorage.updateFilm(film);
    }

    @GetMapping(URL)
    public List<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    @PutMapping(URL + "/{id}/like/{userId}")
    public Film likeFilm(@PathVariable int id, @PathVariable int userId) {
        return filmService.likeFilm(id, userId);
    }

    @DeleteMapping(URL + "/{id}/like/{userId}")
    public Film deleteLike(@PathVariable int id, @PathVariable long userId) throws NotFoundException {
        return filmService.deleteLikeFilm(id, userId);
    }

    @GetMapping(URL + "/popular")
    public List<Film> getFilmWithCount(@RequestParam Optional<Long> count) throws ValidationException {
        if (count.isPresent()) {
            return filmService.getFilmWithCount(count);
        } else {
            return filmService.getFilmWithoutCount();
        }
    }
}
