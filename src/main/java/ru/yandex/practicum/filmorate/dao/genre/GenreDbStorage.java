package ru.yandex.practicum.filmorate.dao.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre createGenre(Genre genre) throws NotFoundException {
        String sqlQuery = "insert into \"genres\" (\"name_genre\") " +
                "values (?)";
        jdbcTemplate.update(sqlQuery, genre.getName());
        return findByName(genre.getName());
    }

    private Genre findByName(String name) throws NotFoundException {
        try {
            return jdbcTemplate.queryForObject("select * from \"genres\" where \"name_genre\" = ?", new GenreRowMapper(), name);
        } catch (Exception e) {
            throw new NotFoundException("Жанр не найден");
        }
    }

    @Override
    public List<Genre> findAllGenre() {
        return jdbcTemplate.query("select * from \"genres\" ORDER BY \"id\"", new GenreRowMapper());
    }

    @Override
    public Genre findGenreById(long id) throws NotFoundException {
        try {
            return jdbcTemplate.queryForObject("select * from \"genres\" WHERE \"id\" = ?", new GenreRowMapper(), id);
        } catch (Exception e) {
            throw new NotFoundException("Жанр не найден");
        }

    }

    @Override
    public Genre updateGenre(Genre genre) throws NotFoundException {
        String sqlQuery = "update \"genres\" set \"name_genre\" = ? where \"id\" = ?";
        jdbcTemplate.update(sqlQuery,
                genre.getName(),
                genre.getId());
        return findByName(genre.getName());
    }
}
