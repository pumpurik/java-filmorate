package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Friendship {
    long id;
    long userId;
    long friendId;
    FriendStatus friendStatus;

    public Friendship(long userId, long friendId, FriendStatus friendStatus) {
        this.userId = userId;
        this.friendId = friendId;
        this.friendStatus = friendStatus;
    }
}
