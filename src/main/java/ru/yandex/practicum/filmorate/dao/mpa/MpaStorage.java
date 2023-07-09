package ru.yandex.practicum.filmorate.dao.mpa;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {
    Mpa createMpa(Mpa mpa) throws NotFoundException;

    List<Mpa> findAllMpa();

    Mpa findMpaById(long id) throws NotFoundException;

    Mpa updateMpa(Mpa mpa) throws NotFoundException;
}
