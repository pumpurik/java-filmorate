package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @PostMapping
    public Genre createGenre(@RequestBody Genre genre) throws NotFoundException {
        return genreService.createGenre(genre);
    }

    @GetMapping("/{id}")
    public Genre getGenre(@PathVariable long id) throws NotFoundException {
        return genreService.getGenre(id);
    }

    @PutMapping
    public Genre updateGenre(@RequestBody Genre genre) throws NotFoundException {
        return genreService.updateGenre(genre);
    }

    @GetMapping
    public List<Genre> findAllGenres() {
        return genreService.findAllGenres();
    }


}
