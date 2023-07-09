package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.friendships.FriendshipStorage;
import ru.yandex.practicum.filmorate.dao.users.UserStorage;
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
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private UserStorage userStorage;
    private FriendshipStorage friendshipStorage;

    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, FriendshipStorage friendshipStorage) {
        this.userStorage = userStorage;
        this.friendshipStorage = friendshipStorage;
    }

    public User createUser(User user) throws ValidationException, NotFoundException {
        validateBirthday(user);
        validateLogin(user);
        validateName(user);
        validateEmail(user);
        return userStorage.createUser(user);
    }

    public User getUser(long id) throws NotFoundException {
        return userStorage.getUser(id);
    }

    public User updateUser(User user) throws ValidationException, NotFoundException {
        validateBirthday(user);
        validateLogin(user);
        validateName(user);
        validateEmail(user);
        return userStorage.updateUser(user);
    }

    public List<User> findAllUsers() {
        return userStorage.findAllUsers();
    }

    public User addFriend(long id, long friendId) throws NotFoundException {

        User user = userStorage.getUser(id);
        User friend = userStorage.getUser(friendId);
        Optional<Friendship> findFriendship = Optional.empty();
        Friendship nonOptionalFriendship = null;
        if (friend.getFriends() != null && !friend.getFriends().isEmpty()) {
            findFriendship = friend.getFriends().stream().filter(f -> f.getUserId() == id && f.getFriendId() == friendId).findFirst();
            findFriendship.ifPresent(friendship -> friendship.setFriendStatus(FriendStatus.CONFIRMED));
        }
        if (findFriendship.isPresent()) {
            nonOptionalFriendship = findFriendship.get();
        }
        nonOptionalFriendship = findFriendship.orElse(new Friendship(id, friendId, FriendStatus.NOT_CONFIRMED));
        user.getFriends().add(nonOptionalFriendship);

        //  friend.getFriends().add(id);
        log.info("Друг добавлен у пользователя c id {}: {}", id, user);
        log.info("Друг добавлен у пользователя c id {}: {}", friendId, friend);
        User saveUser = userStorage.getUser(id);
        friendshipStorage.addFriend(nonOptionalFriendship);
        return saveUser;
    }

    public User deleteFriend(long id, Long friendId) throws NotFoundException {
        friendshipStorage.deleteFriend(id, friendId);
        return userStorage.getUser(id);
    }

    public List<User> getAllFriends(long id) throws NotFoundException {
        List<User> friends = userStorage.getFriendByUserId(id);
        log.info("Возвращаем друзей у пользователя c id {}: {}", id, friends);
        return friends;
    }

    public List<User> getCommonFriends(long id, long otherId) throws NotFoundException {
        User user = userStorage.getUser(id);
        User otherUser = userStorage.getUser(otherId);
        if (user.getFriends() == null || otherUser.getFriends() == null) {
            return Collections.emptyList();
        }
        List<User> commonFriends = user.getFriends().stream()
                .filter(f -> otherUser.getFriends().stream().anyMatch(o -> o.getFriendId() == f.getFriendId()))
                .map(f -> {
                    try {
                        return userStorage.getUser(f.getFriendId());
                    } catch (NotFoundException e) {
                        log.info("Пользователь не найден {}", f);
                        return null;
                    }
                })
                .collect(Collectors.toList());
        log.info("Возвращаем общих друзей у пользователей с id {} , {}: {}", id, otherId, commonFriends);
        return commonFriends;

    }

    private void validateEmail(User user) throws ValidationException {
        if (user.getEmail().isBlank() || user.getEmail() == null || !user.getEmail().contains("@")) {
            log.info("Ошибка почты пользователя. Электронная почта не может быть пустой и должна содержать символ @");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
    }

    private void validateLogin(User user) throws ValidationException {
        if (user.getLogin().isBlank() || user.getLogin() == null || user.getLogin().contains(" ")) {
            log.info("Ошибка логина. Логин не может быть пустым и содержать пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
    }

    private void validateName(User user) throws ValidationException {
        if ((user.getName() == null || user.getName().isBlank()) && user.getLogin() != null && !user.getLogin().isBlank()) {
            user.setName(user.getLogin());
            log.info("Ошибка имени пользователя. Имя для отображения может быть пустым, поменяли на логин");
        } else if ((user.getName() == null || user.getName().isBlank()) && (user.getLogin() == null || user.getLogin().isBlank())) {
            log.info("Ошибка имени пользователя. Имя для отображения может быть пустым, на логин не поменяли");
            throw new ValidationException("Имя для отображения может быть пустым");
        }
    }

    private void validateBirthday(User user) throws ValidationException {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthdayDate = LocalDate.parse(user.getBirthday(), formatter);
        if (birthdayDate.isAfter(date)) {
            log.info("Ошибка даты рождения пользователя. Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
