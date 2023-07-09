package ru.yandex.practicum.filmorate.dao.users;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component("inMemoryUserStorage")
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private Map<Long, User> users = new HashMap<>();
    private long id;


    @Override
    public User createUser(User user) throws ValidationException {

        user.setId(++id);
        user.setFriends(new LinkedHashSet<>());
        users.put(user.getId(), user);
        log.info("Пользователь добавлен: {}", users.get(user.getId()));
        return users.get(user.getId());
    }

    @Override
    public User getUser(long id) throws NotFoundException {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new NotFoundException(String.format("Пользователя с id  %s нет в списке", id));
        }
    }

    @Override
    public User updateUser(User user) throws ValidationException, NotFoundException {

        if (users.containsKey(user.getId())) {
            if (user.getFriends() == null) {
                user.setFriends(new LinkedHashSet<>());
            }
            users.replace(user.getId(), user);
            log.info("Пользователь обновлен: {}", users.get(user.getId()));
            return users.get(user.getId());
        } else {
            log.info("Такого пользователя нет в списке: {}", user.getId());
            throw new NotFoundException(String.format("Пользователя с id %s нет в списке", user.getId()));
        }
    }

    @Override
    public List<User> findAllUsers() {
        if (users.size() == 0) {
            log.info("Cписок пользователей пуст");
            return Collections.emptyList();
        } else {
            log.info("Текущее кол-во пользователей: {}", users.size());
            return new ArrayList<>(users.values());
        }
    }

    @Override
    public List<User> getFriendByUserId(long id) {
        return users.get(id).getFriends().stream()
                .map(f -> users.get(f)).distinct().collect(Collectors.toList());
    }

    @Override
    public void removeUser(long id) throws NotFoundException {
        if (users.containsKey(id)) {
            users.remove(id);
        } else throw new NotFoundException(String.format("Пользователя с id  %s нет в списке", id));
    }


}
