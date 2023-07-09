package ru.yandex.practicum.filmorate.dao.users;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.friendships.FriendshipStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.FriendStatus;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final UserDbStorage userDbStorage;
    private final FriendshipStorage friendshipStorage;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Test
    void createUser() throws ValidationException, NotFoundException {
        LocalDate now = LocalDate.now();
        User user = userDbStorage.createUser(new User(3, "pomogite@", "login", "name123", now.format(formatter), Collections.emptySet()));
        User findCreateUser = userDbStorage.getUser(user.getId());
        assertNotNull(user);
        assertThat(user).isEqualTo(findCreateUser);
    }

    @Test
    void findAllUsers() throws ValidationException {
        LocalDate now = LocalDate.now();
        User user = userDbStorage.createUser(new User(1, "pomogite@1", "login", "name123", now.format(formatter), Collections.emptySet()));
        List<User> users = userDbStorage.findAllUsers();
        assertNotNull(users);
        assertThat(users).isNotEmpty().contains(user);
    }

    @Test
    void removeUser() throws ValidationException {
        LocalDate now = LocalDate.now();
        User user = userDbStorage.createUser(new User(1, "pomogite@2", "login", "name123", now.format(formatter), Collections.emptySet()));
        userDbStorage.removeUser(user.getId());

        assertThrows(NotFoundException.class, () -> {
            userDbStorage.getUser(user.getId());
        });
    }

    @Test
    void updateUser() throws ValidationException, NotFoundException {
        LocalDate now = LocalDate.now();
        User user = userDbStorage.createUser(new User(1, "pomogite@3", "login", "name123", now.format(formatter), Collections.emptySet()));
        userDbStorage.updateUser(new User(user.getId(), "newUser", "newLogin", "newName", now.format(formatter), Collections.emptySet()));
        User findUpdateUser = userDbStorage.getUser(user.getId());
        assertNotNull(findUpdateUser);
        assertThat(Optional.of(findUpdateUser)).hasValueSatisfying(f -> {
            assertThat(f).hasFieldOrPropertyWithValue("id", user.getId());
            assertThat(f).hasFieldOrPropertyWithValue("login", "newLogin");
            assertThat(f).hasFieldOrPropertyWithValue("name", "newName");
        });
    }

    @Test
    void getUser() throws ValidationException, NotFoundException {
        LocalDate now = LocalDate.now();
        User user = userDbStorage.createUser(new User(1, "pomogite@4", "login", "name123", now.format(formatter), Collections.emptySet()));
        User getUser = userDbStorage.getUser(user.getId());
        assertNotNull(getUser);
        assertThat(Optional.of(getUser)).hasValueSatisfying(f -> {
            assertThat(f).hasFieldOrPropertyWithValue("id", user.getId());
        });
    }

    @Test
    void getEmptyFriendByUserId() throws ValidationException {
        LocalDate now = LocalDate.now();
        User user = userDbStorage.createUser(new User(1, "pomogite@6", "login", "name123", now.format(formatter), Collections.emptySet()));
        List<User> friendByUserId = userDbStorage.getFriendByUserId(user.getId());
        assertThat(friendByUserId).isEmpty();
    }

    @Test
    void getFriendByUserId() throws ValidationException {
        LocalDate now = LocalDate.now();
        User user = userDbStorage.createUser(new User(1, "pomogite@7", "login", "name123", now.format(formatter), Collections.emptySet()));
        User friend = userDbStorage.createUser(new User(1, "newPomogite@8", "newLogin", "name123", now.format(formatter), Collections.emptySet()));
        Friendship friendship = friendshipStorage.addFriend(new Friendship(user.getId(), friend.getId(), FriendStatus.NOT_CONFIRMED));
        List<User> friendsByUserId = userDbStorage.getFriendByUserId(friendship.getUserId());
        assertThat(friendsByUserId).isNotEmpty();
        assertThat(friendsByUserId).contains(friend);
    }
}