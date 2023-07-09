package ru.yandex.practicum.filmorate.dao.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.likes.LikesDbStorage;
import ru.yandex.practicum.filmorate.dao.users.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;

    private final LikesDbStorage likesStorage;
    private final UserDbStorage userDbStorage;

    @Test
    void createFilm() throws ValidationException, NotFoundException {
        Film film = filmDbStorage.createFilm(new Film(1, "name", "description", "1999-04-30", 120, 0,
                null, new Mpa(1, "G")));
        Film findCreateFilm = filmDbStorage.getFilmById(film.getId());
        assertNotNull(findCreateFilm);
    }

    @Test
    void getFilmById() throws ValidationException, NotFoundException {
        Film film = filmDbStorage.createFilm(new Film(1, "name", "description", "1999-04-30", 120, 0,
                null, new Mpa(1, "G")));
        Film getFilm = filmDbStorage.getFilmById(film.getId());
        assertNotNull(getFilm);
        assertThat(Optional.of(getFilm)).hasValueSatisfying(f -> {
            assertThat(f).hasFieldOrPropertyWithValue("id", film.getId());
        });
    }

    @Test
    void findAllFilms() throws ValidationException, NotFoundException {
        Film film = filmDbStorage.createFilm(new Film(1, "name", "description", "1999-04-30", 120, 0,
                null, new Mpa(1, "G")));
        List<Film> films = filmDbStorage.findAllFilms();
        assertNotNull(films);
        assertThat(films).isNotEmpty().contains(film);
    }

    @Test
    void getFilmPopular() throws ValidationException, NotFoundException {
        Film film1 = filmDbStorage.createFilm(new Film(1, "name", "description", "1999-04-30", 120, 0,
                null, new Mpa(1, "G")));
        Film film2 = filmDbStorage.createFilm(new Film(2, "newName", "newDescription", "2000-04-30", 200, 1,
                null, new Mpa(1, "G")));
        User user = new User();
        user.setId(1);
        user.setEmail("email");
        userDbStorage.createUser(user);
        likesStorage.addLike(film2, user);
        List<Film> filmPopular = filmDbStorage.getFilmPopular(4);
        assertNotNull(filmPopular);
        film2.setLikes(1);
        assertThat(filmPopular).isNotEmpty();
        assertThat(filmPopular.get(0)).isEqualTo(film2);
    }

    @Test
    void updateFilm() throws ValidationException, NotFoundException {
        Film film = filmDbStorage.createFilm(new Film(1, "name", "description", "1999-04-30", 120, 0,
                null, new Mpa(1, "G")));

        filmDbStorage.updateFilm(new Film(1, "newName", "newDescription", "2000-04-30", 200, 0,
                null, new Mpa(1, "G")));
        Film findUpdateFilm = filmDbStorage.getFilmById(film.getId());
        assertNotNull(findUpdateFilm);
        assertThat(Optional.of(findUpdateFilm)).hasValueSatisfying(f -> {
            assertThat(f).hasFieldOrPropertyWithValue("id", 1L);
            assertThat(f).hasFieldOrPropertyWithValue("name", "newName");
            assertThat(f).hasFieldOrPropertyWithValue("description", "newDescription");
            assertThat(f).hasFieldOrPropertyWithValue("releaseDate", "2000-04-30");
            assertThat(f).hasFieldOrPropertyWithValue("duration", 200L);
            assertThat(f).hasFieldOrPropertyWithValue("mpa.name", "G");
        });
    }
}