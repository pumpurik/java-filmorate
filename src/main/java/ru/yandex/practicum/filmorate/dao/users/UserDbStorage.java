package ru.yandex.practicum.filmorate.dao.users;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.friendships.FriendshipStorage;
import ru.yandex.practicum.filmorate.dao.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

@Component
@Qualifier("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FriendshipStorage friendshipStorage;

    public UserDbStorage(JdbcTemplate jdbcTemplate, FriendshipStorage friendshipStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.friendshipStorage = friendshipStorage;
    }

    @Override
    public User createUser(User user) throws ValidationException {
        String sqlQuery = "INSERT INTO \"users\" (\"email\", \"login\", \"name\", \"birthday\") values (?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        return findByEmail(user.getEmail());
    }

    private User findByEmail(String email) {
        String sqlQuery = "SELECT * from \"users\" WHERE \"email\" = ?";
        return jdbcTemplate.queryForObject(sqlQuery, new UserRowMapper(), email);
    }

    @Override
    public List<User> findAllUsers() {
        return jdbcTemplate.query("select * from \"users\"", new UserRowMapper());
    }

    @Override
    public void removeUser(long id) {
        jdbcTemplate.update("DELETE FROM \"users\" WHERE \"id\" = ?", id);
    }

    @Override
    public User updateUser(User user) throws ValidationException, NotFoundException {
        String sqlQuery = "update \"users\" set \"email\" = ?, \"login\" = ?, \"name\" = ?, \"birthday\" = ? where \"id\" = ?";
        int update = jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return getUser(update);
    }

    @Override
    public User getUser(long id) throws NotFoundException {
        String sqlQuery = "select * from \"users\"  where \"id\" = ?";
        try {
            User user = jdbcTemplate.queryForObject(sqlQuery, new UserRowMapper(), id);
            user.setFriends(new LinkedHashSet<>(friendshipStorage.getAllFriendsByUser(id)));
            return user;
        } catch (Exception e) {
            throw new NotFoundException(String.format("Пользователь не найден: %s", id));
        }

    }

    @Override
    public List<User> getFriendByUserId(long id) {
        String sqlQuery = "SELECT \"u\".\"id\", \"u\".\"email\", \"u\".\"login\", \"u\".\"name\", \"u\".\"birthday\" FROM \"users\" AS \"u\" \n" +
                "                 where \"u\".\"id\" IN (SELECT \"friends_id\" FROM \"friendship\" \n" +
                "                 where \"user_id\" = ?);";
        try {
            List<User> query = jdbcTemplate.query(sqlQuery, new UserRowMapper(), id);
            return query;
        } catch (Exception e) {
            return Collections.emptyList();
        }


    }
}
