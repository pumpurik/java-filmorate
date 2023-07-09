package ru.yandex.practicum.filmorate.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.FriendStatus;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FriendshipRowMapper implements RowMapper<Friendship> {
    @Override
    public Friendship mapRow(ResultSet rs, int rowNum) throws SQLException {
        Friendship friendship = new Friendship();
        friendship.setId(rs.getLong("id"));
        friendship.setUserId(rs.getLong("user_id"));
        friendship.setFriendId(rs.getLong("friends_id"));
        friendship.setFriendStatus(FriendStatus.valueOf(rs.getString("friends_status")));
        return friendship;
    }
}
