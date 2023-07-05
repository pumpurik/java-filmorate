package ru.yandex.practicum.filmorate.storage;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@Data
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Integer, Film> films = new HashMap<>();
    int id;

    @Override
    public void updateFilms(Film film) {
        films.replace(film.getId(), film);
    }

    @Override
    public Film createFilm(Film film) throws ValidationException {
        validateName(film);
        validateDescription(film);
        validateDuration(film);
        validateReleaseDate(film);
        film.setId(++id);
        film.setLikes(0);
        films.put(film.getId(), film);
        log.info("Фильм добавлен: {}", films.get(film.getId()));
        return films.get(film.getId());
    }

    @Override
    public Film getFilm(int id) throws NotFoundException {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new NotFoundException("Фильма с id " + id + " нет в списке");
        }
    }

    @Override
    public Film updateFilm(Film film) throws ValidationException, NotFoundException {
        validateName(film);
        validateDescription(film);
        validateDuration(film);
        validateReleaseDate(film);
        if (films.containsKey(film.getId())) {
            films.replace(film.getId(), film);
            log.info("Фильм обновлен: {}", films.get(film.getId()));
            return films.get(film.getId());
        } else {
            log.info("Такого фильма нет в списке: {}", film.getId());
            throw new NotFoundException("Фильма с id " + film.getId() + " нет в списке");
        }
    }

    @Override
    public List<Film> findAllFilms() {
        log.info("Текущее кол-во фильмов: {}", films.size());
        return new ArrayList<>(films.values());
    }

    private void validateName(Film film) throws ValidationException {
        if (film.getName() == null || film.getName().isBlank()) {
            log.info("Ошибка названия фильма");
            throw new ValidationException("Название не может быть пустым");
        }
    }

    private void validateDescription(Film film) throws ValidationException {
        if (film.getDescription().length() > 200) {
            log.info("Ошибка описания фильма");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
    }

    private void validateReleaseDate(Film film) throws ValidationException {
        LocalDate date = LocalDate.of(1895, 11, 28);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate realiseDate = LocalDate.parse(film.getReleaseDate(), formatter);
        if (realiseDate.isBefore(date)) {
            log.info("Ошибка даты релиза фильма");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
    }

    private void validateDuration(Film film) throws ValidationException {
        if (film.getDuration() < 1) {
            log.info("Ошибка продолжительности фильма");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }

    }
}
