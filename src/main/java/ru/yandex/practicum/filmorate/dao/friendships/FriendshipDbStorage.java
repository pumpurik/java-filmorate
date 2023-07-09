package ru.yandex.practicum.filmorate.dao.friendships;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.mapper.FriendshipRowMapper;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FriendshipDbStorage implements FriendshipStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Friendship addFriend(Friendship friendship) {
        String sqlQuery = "insert into \"friendship\" (\"user_id\", \"friends_id\", \"friends_status\") " +
                "values (?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                friendship.getUserId(),
                friendship.getFriendId(),
                friendship.getFriendStatus().toString());
        return friendship;
    }

    @Override
    public boolean deleteFriend(long id, Long friendId) {
        String sqlQuery = "delete from \"friendship\" where \"user_id\" = ? AND \"friends_id\" = ?";
        return jdbcTemplate.update(sqlQuery, id, friendId) > 0;
    }

    @Override
    public List<Friendship> getAllFriendsByUser(long id) {
        String sqlQuery = "select * from \"friendship\" where \"user_id\" = ?";
        return jdbcTemplate.query(sqlQuery, new FriendshipRowMapper(), id);//я не ебу уже блять
    }

}
