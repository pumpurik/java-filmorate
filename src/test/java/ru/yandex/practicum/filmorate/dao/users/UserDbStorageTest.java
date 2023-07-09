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
        User user = userDbStorage.createUser(new User(1, "pomogite@", "login", "name123", now.format(formatter), Collections.emptySet()));
        User findCreateUser = userDbStorage.getUser(1);
        assertNotNull(user);
        assertThat(user).isEqualTo(findCreateUser);
    }

    @Test
    void findAllUsers() throws ValidationException {
        LocalDate now = LocalDate.now();
        User user = userDbStorage.createUser(new User(1, "pomogite@", "login", "name123", now.format(formatter), Collections.emptySet()));
        List<User> users = userDbStorage.findAllUsers();
        assertNotNull(users);
        assertThat(users).isNotEmpty().contains(user);
    }

    @Test
    void removeUser() throws ValidationException, NotFoundException {
        LocalDate now = LocalDate.now();
        User user = userDbStorage.createUser(new User(1, "pomogite@", "login", "name123", now.format(formatter), Collections.emptySet()));
        userDbStorage.removeUser(1L);

        assertThrows(NotFoundException.class, () -> {
            User findUser = userDbStorage.getUser(1L);
        });
    }

    @Test
    void updateUser() throws ValidationException, NotFoundException {
        LocalDate now = LocalDate.now();
        User user = userDbStorage.createUser(new User(1, "pomogite@", "login", "name123", now.format(formatter), Collections.emptySet()));
        userDbStorage.updateUser(new User(1, "newUser", "newLogin", "newName", now.format(formatter), Collections.emptySet()));
        User findUpdateUser = userDbStorage.getUser(1);
        assertNotNull(findUpdateUser);
        assertThat(Optional.of(findUpdateUser)).hasValueSatisfying(f -> {
            assertThat(f).hasFieldOrPropertyWithValue("id", 1L);
            assertThat(f).hasFieldOrPropertyWithValue("email", "newUser");
            assertThat(f).hasFieldOrPropertyWithValue("login", "newLogin");
            assertThat(f).hasFieldOrPropertyWithValue("name", "newName");
        });
    }

    @Test
    void getUser() throws ValidationException, NotFoundException {
        LocalDate now = LocalDate.now();
        User user = userDbStorage.createUser(new User(1, "pomogite@", "login", "name123", now.format(formatter), Collections.emptySet()));
        User getUser = userDbStorage.getUser(1);
        assertNotNull(getUser);
        assertThat(Optional.of(getUser)).hasValueSatisfying(f -> {
            assertThat(f).hasFieldOrPropertyWithValue("id", 1L);
        });
    }

    @Test
    void getEmptyFriendByUserId() throws ValidationException {
        LocalDate now = LocalDate.now();
        User user = userDbStorage.createUser(new User(1, "pomogite@", "login", "name123", now.format(formatter), Collections.emptySet()));
        List<User> friendByUserId = userDbStorage.getFriendByUserId(1L);
        assertThat(friendByUserId).isEmpty();
    }

    @Test
    void getFriendByUserId() throws ValidationException {
        LocalDate now = LocalDate.now();
        User user = userDbStorage.createUser(new User(1, "pomogite@", "login", "name123", now.format(formatter), Collections.emptySet()));
        User friend = userDbStorage.createUser(new User(2, "newPomogite@", "newLogin", "name123", now.format(formatter), Collections.emptySet()));
        friendshipStorage.addFriend(new Friendship(1, 2, FriendStatus.NOT_CONFIRMED));
        List<User> friendsByUserId = userDbStorage.getFriendByUserId(1L);
        assertThat(friendsByUserId).isNotEmpty();
        assertThat(friendsByUserId).contains(friend);
    }
}