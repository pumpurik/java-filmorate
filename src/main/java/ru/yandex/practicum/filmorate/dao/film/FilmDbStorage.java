package ru.yandex.practicum.filmorate.dao.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.utils.NullUtils.nonNullSet;

@Component("filmDbStorage")
@Qualifier
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");


    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

    }

    private void addGenre(long genreId, long filmId) {
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) AS count FROM \"genres_films\" WHERE \"genres_id\" = ? AND \"films_id\" = ?", (rs, rowNum) -> {
            return rs.getLong("count");
        }, genreId, filmId);

        if (count == null || count <= 0) {
            String sqlQuery = "INSERT INTO \"genres_films\" (\"genres_id\", \"films_id\") VALUES(?,?)";
            jdbcTemplate.update(sqlQuery, genreId, filmId);
        }
    }


    @Override
    public Film createFilm(Film film) throws ValidationException, NotFoundException {
        String sqlQueryFilm = "insert into \"films\" (\"mpa_id\", \"name\", \"description\", \"releaseDate\", \"duration\") " +
                "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement pst = connection.prepareStatement(sqlQueryFilm, Statement.RETURN_GENERATED_KEYS);
            pst.setLong(1, film.getMpa().getId());
            pst.setString(2, film.getName());
            pst.setString(3, film.getDescription());
            try {
                java.util.Date parse = formatter.parse(film.getReleaseDate());
                pst.setDate(4, new Date(parse.getTime()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            pst.setLong(5, film.getDuration());
            return pst;
        }, keyHolder);
        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        List<Genre> collect = nonNullSet(film.getGenres()).stream().peek(g -> addGenre(g.getId(), id)).collect(Collectors.toList());

        return getFilmById(id);
    }

    private Film findByDescriptionAndName(String name, String description) {
        String sqlQuery = "SELECT * from \"films\" WHERE \"description\" = ? AND \"name\" = ?";
        return jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> null, description, name);
    }

    @Override
    public Film getFilmById(long id) throws NotFoundException {
        String sqlQuery = "SELECT \"f\".\"id\", \"f\".\"mpa_id\", \"f\".\"name\", \"f\".\"description\", \"f\".\"releaseDate\", \"f\".\"duration\", COUNT(\"l\".\"id\") AS \"likes\", \"m\".\"name\" AS \"mpa_name\",\n" +
                "from PUBLIC.\"films\" AS \"f\" \n" +
                "LEFT JOIN PUBLIC.\"mpa\" AS \"m\" on \"f\".\"mpa_id\" = \"m\".\"id\" \n" +
                "LEFT JOIN PUBLIC.\"likes\" AS \"l\" ON \"f\".\"id\" = \"l\".\"film_id\" \n" +
                "WHERE \"f\".\"id\" = ? GROUP  BY \"f\".\"id\"";
        try {
            Film film = jdbcTemplate.queryForObject(sqlQuery, new FilmRowMapper(), id);
            Set<Genre> genreByFilmId = getGenreByFilmId(id);

            film.setGenres(genreByFilmId);
            return film;
        } catch (Exception e) {
            throw new NotFoundException("Фильм не найден");
        }

    }

    @Override
    public List<Film> findAllFilms() {
        String query = "SELECT \"f\".\"id\", \"f\".\"mpa_id\", \"f\".\"name\", \"f\".\"description\", \"f\".\"releaseDate\", \"f\".\"duration\", COUNT(\"l\".\"id\") AS \"likes\", \"m\".\"name\" AS \"mpa_name\",\n" +
                "FROM PUBLIC.\"films\" AS \"f\" \n" +
                "LEFT JOIN PUBLIC.\"mpa\" AS \"m\" on \"f\".\"mpa_id\" = \"m\".\"id\" \n" +
                "LEFT JOIN PUBLIC.\"likes\" AS \"l\" ON \"f\".\"id\" = \"l\".\"film_id\" \n" +
                "GROUP  BY \"f\".\"id\"";

        List<Film> query1 = jdbcTemplate.query(query, new FilmRowMapper()).stream().peek(f -> f.setGenres(getGenreByFilmId(f.getId()))).collect(Collectors.toList());
        return query1;
    }

    @Override
    public List<Film> getFilmPopular(int count) {
        String query = "SELECT \"f\".\"id\", \"f\".\"mpa_id\", \"f\".\"name\", \"f\".\"description\", \"f\".\"releaseDate\", \"f\".\"duration\", COUNT(\"l\".\"id\") AS \"likes\", \"m\".\"name\" AS \"mpa_name\",\n" +
                "\"g\".\"id\" AS \"genre_id\", \"g\".\"name_genre\" \n" +
                "FROM PUBLIC.\"films\" AS \"f\" \n" +
                "LEFT JOIN PUBLIC.\"mpa\" AS \"m\" on \"f\".\"mpa_id\" = \"m\".\"id\" \n" +
                "LEFT JOIN PUBLIC.\"genres_films\" AS \"gf\" ON \"f\".\"id\" = \"gf\".\"films_id\"\n" +
                "LEFT JOIN PUBLIC.\"genres\" AS \"g\" ON \"gf\".\"genres_id\" = \"g\".\"id\"\n" +
                "LEFT JOIN PUBLIC.\"likes\" AS \"l\" ON \"f\".\"id\" = \"l\".\"film_id\" \n" +
                "GROUP  BY \"f\".\"id\" ORDER BY \"likes\" DESC LIMIT ?";
        return jdbcTemplate.query(query, new FilmRowMapper(), count);
    }

    private long getCountLikesByFilmId(long filmId) {
        String query = "SELECT COUNT(\"film_id\") AS \"count\" FROM \"likes\" WHERE \"film_id\" = ?";
        return jdbcTemplate.queryForObject(query, (rs, rowNum) -> {
            return rs.getLong("count");
        }, filmId);
    }

    private Set<Genre> getGenreByFilmId(long id) {
        String sqlQuery = "SELECT \"g\".\"id\", \"g\".\"name_genre\" FROM \"genres_films\" AS \"gf\" " +
                "JOIN \"genres\" AS \"g\" ON \"gf\".\"genres_id\" = \"g\".\"id\" " +
                "WHERE \"gf\".\"films_id\" = ? ORDER BY \"g\".\"id\"";
        return new HashSet<>(jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
            Genre genre = new Genre();
            genre.setId(rs.getLong("id"));
            genre.setName(rs.getString("name_genre"));
            return genre;
        }, id));
    }

    @Override
    public Film updateFilm(Film film) throws ValidationException, NotFoundException {
        String sqlQuery = "update \"films\" set \"mpa_id\" = ?, \"name\" = ?, \"description\" = ?, \"releaseDate\" = ?, \"duration\" = ? " +
                "WHERE \"id\" = ?";
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement pst = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
                pst.setLong(1, film.getMpa().getId());
                pst.setString(2, film.getName());
                pst.setString(3, film.getDescription());
                try {
                    java.util.Date parse = formatter.parse(film.getReleaseDate());
                    pst.setDate(4, new Date(parse.getTime()));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                pst.setLong(5, film.getDuration());
                pst.setLong(6, film.getId());
                return pst;
            }, keyHolder);
            int id = Objects.requireNonNull(keyHolder.getKey()).intValue();

            removeGereFromFilm(film.getId());
            List<Genre> collect = nonNullSet(film.getGenres()).stream().peek(g -> addGenre(g.getId(), film.getId())).collect(Collectors.toList());
            return getFilmById(id);
        } catch (Exception e) {
            throw new NotFoundException("Фильм не найден");
        }

    }

    private void removeGereFromFilm(long idFilm) {
        jdbcTemplate.update("DELETE FROM \"genres_films\" WHERE \"films_id\" = ?", idFilm);
    }


}
