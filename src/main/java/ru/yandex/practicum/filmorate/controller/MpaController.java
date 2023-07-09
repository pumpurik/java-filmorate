package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {
    private final MpaService mpaService;


    @PostMapping
    public Mpa createGenre(@RequestBody Mpa genre) throws ValidationException, NotFoundException {
        return mpaService.createMpa(genre);
    }

    @GetMapping("/{id}")
    public Mpa getGenre(@PathVariable long id) throws NotFoundException {
        return mpaService.getMpa(id);
    }

    @PutMapping
    public Mpa updateGenre(@RequestBody Mpa genre) throws ValidationException, NotFoundException {
        return mpaService.updateMpa(genre);
    }

    @GetMapping
    public List<Mpa> findAllGenres() throws NotFoundException {
        return mpaService.findAllMpa();
    }

}
