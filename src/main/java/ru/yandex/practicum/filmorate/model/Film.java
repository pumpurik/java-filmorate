package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
public class Film {
    private int id;
    private String name;
    private String description;
    private String releaseDate;
    private long duration;
}
