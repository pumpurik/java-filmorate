package ru.yandex.practicum.filmorate.dao.film;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component("inMemoryFilmStorage")
@Slf4j
@Data
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Long, Film> films = new HashMap<>();
    private int id;

    @Override
    public Film createFilm(Film film) {

        film.setId(++id);
        film.setLikes(0);
        films.put(film.getId(), film);
        log.info("Фильм добавлен: {}", films.get(film.getId()));
        return films.get(film.getId());
    }

    @Override
    public Film getFilmById(long id) throws NotFoundException {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new NotFoundException(String.format("Фильма с id  %s нет в списке", id));
        }
    }

    @Override
    public Film updateFilm(Film film) throws NotFoundException {

        if (films.containsKey(film.getId())) {
            films.replace(film.getId(), film);
            log.info("Фильм обновлен: {}", films.get(film.getId()));
            return films.get(film.getId());
        } else {
            log.info("Такого фильма нет в списке: {}", film.getId());
            throw new NotFoundException(String.format("Фильма с id  %s нет в списке", film.getId()));
        }
    }

    @Override
    public List<Film> findAllFilms() {
        log.info("Текущее кол-во фильмов: {}", films.size());
        return new ArrayList<>(films.values());
    }

    public List<Film> getFilmPopular(int count) {
        List<Film> sortedFilms = films.values().stream()
                .sorted(Comparator.comparing(Film::getId).thenComparing(Film::getLikes).reversed())
                .limit(count)
                .collect(Collectors.toList());
        log.info("Самые популярные фильмы в кол-ве {}: {}", count, sortedFilms);
        return sortedFilms;
    }
}
