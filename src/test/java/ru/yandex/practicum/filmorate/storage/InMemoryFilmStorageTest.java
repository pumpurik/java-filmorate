package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
class InMemoryFilmStorageTest {
    @InjectMocks
    private InMemoryFilmStorage filmService;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Test
    void createFilmValid() throws ValidationException {
        LocalDate now = LocalDate.now();
        Film film = new Film(1, "name", "description", now.format(formatter), 10000L, 0);
        Film film1 = filmService.createFilm(film);
        assertEquals(film1, film);
    }

    @Test
    void createFilmWithoutName() {
        LocalDate now = LocalDate.now();
        Film film = new Film(1, "", "description", now.format(formatter), 10000L, 0);
        ValidationException validationException = assertThrows(ValidationException.class, () -> {
            filmService.createFilm(film);
        });
        assertEquals("Название не может быть пустым", validationException.getMessage());
    }

    @Test
    void createFilmWithNotValidDescription() {
        LocalDate now = LocalDate.now();
        byte[] array = new byte[201];
        String generatedString = new String(array, Charset.forName("UTF-8"));
        System.out.println(generatedString);
        Film film = new Film(1, "Name", generatedString, now.format(formatter), 10000L, 0);
        ValidationException validationException = assertThrows(ValidationException.class, () -> {
            filmService.createFilm(film);
        });
        assertEquals("Максимальная длина описания — 200 символов", validationException.getMessage());
    }

    @Test
    void createFilmNotValidRealiseDate() {
        LocalDate date = LocalDate.of(1894, 11, 28);
        Film film = new Film(1, "name", "description", date.format(formatter), 1, 0);
        ValidationException validationException = assertThrows(ValidationException.class, () -> {
            filmService.createFilm(film);
        });
        assertEquals("Дата релиза — не раньше 28 декабря 1895 года", validationException.getMessage());
    }

    @Test
    void createFilmNotValidDuration() {
        LocalDate now = LocalDate.now();
        Film film = new Film(1, "name", "description", now.format(formatter), 0, 0);
        ValidationException validationException = assertThrows(ValidationException.class, () -> {
            filmService.createFilm(film);
        });
        assertEquals("Продолжительность фильма должна быть положительной", validationException.getMessage());
    }

    @Test
    void updateFilmValid() throws ValidationException, NotFoundException {
        LocalDate now = LocalDate.now();
        Film createFilm = new Film(1, "name", "description", now.format(formatter), 10000L, 0);
        Film updateFilm = new Film(1, "newName", "description", now.format(formatter), 10000L, 0);
        filmService.createFilm(createFilm);
        Film film1 = filmService.updateFilm(updateFilm);
        assertEquals(film1, updateFilm);
    }

    @Test
    void updateFilmNotValid() throws NotFoundException, ValidationException {
        LocalDate now = LocalDate.now();
        Film createFilm = new Film(1, "name", "description", now.format(formatter), 10000L, 0);
        Film updateFilm = new Film(100, "newName", "description", now.format(formatter), 10000L, 0);
        filmService.createFilm(createFilm);
        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> {
            Film film1 = filmService.updateFilm(updateFilm);
        });
        assertEquals("Фильма с id " + updateFilm.getId() + " нет в списке", notFoundException.getMessage());
    }

    @Test
    void findAllFilms() throws ValidationException, NotFoundException {
        LocalDate now = LocalDate.now();
        Film film1 = new Film(1, "film1", "description1", now.format(formatter), 10000L, 0);
        Film film2 = new Film(2, "film2", "description2", now.format(formatter), 20000L, 0);
        filmService.createFilm(film1);
        filmService.createFilm(film2);
        List<Film> testFilms = new ArrayList(Arrays.asList(film1, film2));
        assertEquals(filmService.findAllFilms(), testFilms);
    }
}