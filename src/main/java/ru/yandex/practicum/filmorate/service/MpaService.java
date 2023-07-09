package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Service
@AllArgsConstructor
public class MpaService {
    private final MpaStorage mpaStorage;

    public Mpa createMpa(Mpa genre) throws NotFoundException {
        return mpaStorage.createMpa(genre);
    }

    public Mpa getMpa(long id) throws NotFoundException {
        return mpaStorage.findMpaById(id);
    }

    public Mpa updateMpa(Mpa genre) throws NotFoundException {
        return mpaStorage.updateMpa(genre);
    }

    public List<Mpa> findAllMpa() {
        return mpaStorage.findAllMpa();
    }
}
