package ru.yandex.practicum.filmorate.dao.friendships;

import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.List;

public interface FriendshipStorage {
    public Friendship addFriend(Friendship friendship);

    public boolean deleteFriend(long id, Long friendId);

    public List<Friendship> getAllFriendsByUser(long id);
}
