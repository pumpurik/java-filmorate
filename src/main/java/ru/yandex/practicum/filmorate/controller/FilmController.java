package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) throws ValidationException, NotFoundException {
        return filmService.createFilm(film);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable int id) throws NotFoundException {
        return filmService.getFilmById(id);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws ValidationException, NotFoundException {

        return filmService.updateFilm(film);
    }

    @GetMapping
    public List<Film> findAllFilms() {
        return filmService.findAllFilms();
    }

    @PutMapping("/{id}/like/{userId}")
    public Film likeFilm(@PathVariable int id, @PathVariable int userId) throws ValidationException, NotFoundException {
        return filmService.likeFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable int id, @PathVariable long userId) throws NotFoundException, ValidationException {
        return filmService.deleteLikeFilm(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getFilmWithCount(@RequestParam Optional<Integer> count) throws ValidationException {
        return filmService.getFilmWithCount(count);

    }
}
