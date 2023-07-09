package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.dao.likes.LikesStorage;
import ru.yandex.practicum.filmorate.dao.users.UserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FilmService {
    private FilmStorage filmStorage;
    private UserStorage userStorage;
    private LikesStorage likesStorage;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, @Qualifier("userDbStorage") UserStorage userStorage,
                       @Qualifier("likesDbStorage") LikesStorage likesStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likesStorage = likesStorage;
    }

    public Film getFilmById(long id) throws NotFoundException {
        return filmStorage.getFilmById(id);
    }

    public List<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    public Film createFilm(Film film) throws ValidationException, NotFoundException {
        validateName(film);
        validateDescription(film);
        validateDuration(film);
        validateReleaseDate(film);
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) throws ValidationException, NotFoundException {
        validateName(film);
        validateDescription(film);
        validateDuration(film);
        validateReleaseDate(film);
        Film updateFilm = filmStorage.updateFilm(film);
        return updateFilm;
    }

    public Film likeFilm(long id, long userId) throws NotFoundException, ValidationException {
        Film filmById = filmStorage.getFilmById(id);
        filmById.setLikes(filmById.getLikes() + 1);
        filmStorage.updateFilm(filmById);
        likesStorage.addLike(filmStorage.getFilmById(id), userStorage.getUser(userId));
        return filmById;
    }

    public Film deleteLikeFilm(int id, long userId) throws NotFoundException, ValidationException {
        likesStorage.removeLike(filmStorage.getFilmById(id), userStorage.getUser(userId));
        Film filmById = filmStorage.getFilmById(id);
        filmById.setLikes(filmById.getLikes() - 1);
        filmStorage.updateFilm(filmById);
        return filmStorage.getFilmById(id);
    }


    public List<Film> getFilmWithCount(Optional<Integer> count) throws ValidationException {
        int schet = count.orElse(10);
        if (schet > 0) {
            return filmStorage.getFilmPopular(schet);
        } else {
            throw new ValidationException("Неверное значение count.");
        }
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
