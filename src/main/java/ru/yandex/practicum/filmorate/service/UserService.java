package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(long id, Long friendId) throws NotFoundException {
        if (userStorage.getUsers().containsKey(id) && userStorage.getUsers().containsKey(friendId)) {
            User user1 = userStorage.getUsers().get(id);
            User user2 = userStorage.getUsers().get(friendId);
            user1.getFriends().add(friendId);
            user2.getFriends().add(id);
            log.info("Друг добавлен у пользователя c id {}: {}", id, userStorage.getUsers().get(id));
            log.info("Друг добавлен у пользователя c id {}: {}", friendId, userStorage.getUsers().get(friendId));
            return userStorage.getUsers().get(id);
        } else {
            throw new NotFoundException("Пользователь не найден.");
        }
    }

    public User deleteFriend(long id, Long friendId) {
        User user1 = userStorage.getUsers().get(id);
        User user2 = userStorage.getUsers().get(friendId);
        user1.getFriends().remove(friendId);
        user2.getFriends().remove(id);
        log.info("Друг удален у пользователя c id {}: {}", id, userStorage.getUsers().get(id));
        log.info("Друг удален у пользователя c id {}: {}", friendId, userStorage.getUsers().get(friendId));
        return userStorage.getUsers().get(id);
    }

    public Set<User> getAllFriends(long id) {
        Set<User> friends = new TreeSet<>();
        friends = userStorage.getUsers().get(id).getFriends().stream()
                .map(f -> userStorage.getUsers().get(f))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        log.info("Возвращаем друзей у пользователя c id {}: {}", id, friends);
        return friends;
    }

    public Set<User> getCommonFriends(long id, long otherId) {
        User user = userStorage.getUsers().get(id);
        User otherUser = userStorage.getUsers().get(otherId);
        Set<User> commonFriends = user.getFriends().stream()
                .filter(f -> otherUser.getFriends().contains(f))
                .map(f -> userStorage.getUsers().get(f))
                .collect(Collectors.toSet());
        log.info("Возвращаем общих друзей у пользователей с id {} , {}: {}", id, otherId, commonFriends);
        return commonFriends;

    }
}
