package ru.yandex.practicum.filmorate.dao.likes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

@Component("likesDbStorage")
@Slf4j
public class LikesDbStorage implements LikesStorage {
    private final JdbcTemplate jdbcTemplate;

    public LikesDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(Film film, User user) {
        String query = "INSERT INTO \"likes\" (\"user_id\", \"film_id\") VALUES(?, ?)";
        jdbcTemplate.update(query, user.getId(), film.getId());
    }

    @Override
    public void removeLike(Film film, User user) {
        String query = "DELETE FROM \"likes\" WHERE \"user_id\" = ?,  \"film_id\" = ?";
    }
}
