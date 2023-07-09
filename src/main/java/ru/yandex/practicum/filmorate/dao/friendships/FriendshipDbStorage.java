package ru.yandex.practicum.filmorate.dao.friendships;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.mapper.FriendshipRowMapper;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class FriendshipDbStorage implements FriendshipStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Friendship addFriend(Friendship friendship) {
        String sqlQuery = "insert into \"friendship\" (\"user_id\", \"friends_id\", \"friends_status\") " +
                "values (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement pst = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            pst.setLong(1, friendship.getUserId());
            pst.setLong(2, friendship.getFriendId());
            pst.setString(3, friendship.getFriendStatus().toString());
            return pst;
        }, keyHolder);
        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        return getFriendById(id);
    }

    @Override
    public boolean deleteFriend(long id, Long friendId) {
        String sqlQuery = "delete from \"friendship\" where \"user_id\" = ? AND \"friends_id\" = ?";
        return jdbcTemplate.update(sqlQuery, id, friendId) > 0;
    }

    private Friendship getFriendById(long id) {
        String sqlQuery = "select * from \"friendship\" where \"id\" = ?";
        return jdbcTemplate.queryForObject(sqlQuery, new FriendshipRowMapper(), id);//я не ебу уже блять
    }

    @Override
    public List<Friendship> getAllFriendsByUser(long id) {
        String sqlQuery = "select * from \"friendship\" where \"user_id\" = ?";
        return jdbcTemplate.query(sqlQuery, new FriendshipRowMapper(), id);//я не ебу уже блять
    }

}
