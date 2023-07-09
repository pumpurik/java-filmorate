package ru.yandex.practicum.filmorate.dao.mpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.mapper.MpaRowMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Mpa createMpa(Mpa mpa) throws NotFoundException {
        String sqlQuery = "insert into \"mpa\" (\"name\") " +
                "values (?)";
        jdbcTemplate.update(sqlQuery, mpa.getName());
        return findByName(mpa.getName());
    }

    private Mpa findByName(String name) throws NotFoundException {
        try {
            return jdbcTemplate.queryForObject("select * from \"mpa\" where \"name\" = ?", new MpaRowMapper(), name);
        } catch (Exception e) {
            throw new NotFoundException("Возрастное ограничение не найдено");
        }
    }

    @Override
    public List<Mpa> findAllMpa() {
        return jdbcTemplate.query("select * from \"mpa\" ORDER BY \"id\"", new MpaRowMapper());
    }

    @Override
    public Mpa findMpaById(long id) throws NotFoundException {
        try {
            return jdbcTemplate.queryForObject("select * from \"mpa\" where \"id\" = ?", new MpaRowMapper(), id);
        } catch (Exception e) {
            throw new NotFoundException("Возрастное ограничение не найдено");
        }

    }

    @Override
    public Mpa updateMpa(Mpa mpa) throws NotFoundException {
        String sqlQuery = "update \"mpa\" set \"name\" = ? where \"id\" = ?";
        jdbcTemplate.update(sqlQuery,
                mpa.getName(),
                mpa.getId());
        return findByName(mpa.getName());
    }
}
