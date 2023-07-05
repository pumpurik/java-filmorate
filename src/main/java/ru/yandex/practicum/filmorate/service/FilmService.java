package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private FilmStorage filmStorage;
    private UserStorage userStorage;
    private LikesStorage likesStorage;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage, LikesStorage likesStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likesStorage = likesStorage;
    }

    public Film likeFilm(int id, long userId) {
        filmStorage.getFilms().get(id).setLikes(filmStorage.getFilms().get(id).getLikes() + 1);
        likesStorage.getLikes().put(filmStorage.getFilms().get(id), userStorage.getUsers().get(userId));
        if (likesStorage.getLikes().containsKey(filmStorage.getFilms().get(id))) {
            log.info("Пользователь c id " + userId + " лайкнул фильм с id " + id + ": {}", filmStorage.getFilms().get(id));
            return filmStorage.getFilms().get(id);
        } else {
            return null;
        }
    }

    public Film deleteLikeFilm(int id, long userId) throws NotFoundException {
        if (userStorage.getUsers().containsKey(userId) && filmStorage.getFilms().containsKey(id)) {
            likesStorage.getLikes().remove(filmStorage.getFilms().get(id), userStorage.getUsers().get(userId));
            filmStorage.getFilms().get(id).setLikes(filmStorage.getFilms().get(id).getLikes() - 1);
            log.info("Пользователь c id " + userId + " удалил лайк с фильма с id " + id + ": {}", filmStorage.getFilms().get(id));
            return filmStorage.getFilms().get(id);
        } else if (!userStorage.getUsers().containsKey(userId)) {
            throw new NotFoundException("Фильм с таким айди не найден.");
        } else {
            throw new NotFoundException("Пользователь с таким айди не найден.");
        }
    }

    public List<Film> getFilmWithCount(Optional<Long> count) throws ValidationException {
        long schet = count.get();
        if (schet > 0) {
            List<Film> sortedFilms = filmStorage.getFilms().values().stream()
                    .sorted(Comparator.comparing(Film::getId).thenComparing(Film::getLikes).reversed())
                    .limit(schet)
                    .collect(Collectors.toList());
            log.info("Самые популярные фильмы в кол-ве " + count + ": {}", sortedFilms);
            return sortedFilms;
        } else {
            throw new ValidationException("Неверное значение count.");
        }
    }

    public List<Film> getFilmWithoutCount() {
        List<Film> sortedFilms = filmStorage.getFilms().values().stream()
                .sorted(Comparator.comparing(Film::getId).thenComparing(Film::getLikes).reversed())
                .limit(10)
                .collect(Collectors.toList());
        log.info("Самые популярные фильмы в кол-ве 10: {}", sortedFilms);
        return sortedFilms;
    }
}
