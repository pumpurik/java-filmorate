package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = FilmController.class)
class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FilmService filmService;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private Film film;

    @BeforeEach
    void setUp() {
        LocalDate now = LocalDate.now();

        film = new Film(1, "name", "description", now.format(formatter), 10000l);
    }

    @Test()
    void createFilm() throws Exception {
        when(filmService.createFilm(any())).thenReturn(film);
        MvcResult result = mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON).content(asJsonString(film)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertNotNull(result);
        String actualFilm = result.getResponse().getContentAsString();
        assertEquals(film, jsonToObject(actualFilm, Film.class));
    }

    @Test
    void updateFilm() throws Exception {
        when(filmService.updateFilm(any())).thenReturn(film);
        MvcResult result = mockMvc.perform(put("/films").contentType(MediaType.APPLICATION_JSON).content(asJsonString(film)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertNotNull(result);
        String actualFilm = result.getResponse().getContentAsString();
        assertEquals(film, jsonToObject(actualFilm, Film.class));
    }

    @Test
    void findAllFilms() throws Exception {
        when(filmService.findAllFilms()).thenReturn(List.of(film));
        MvcResult result = mockMvc.perform(get("/films").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertNotNull(result);
        String actualFilm = result.getResponse().getContentAsString();
        assertEquals(1, jsonToObject(actualFilm, List.class).size());
    }

    public static String asJsonString(Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T jsonToObject(String str, Class<T> cl) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(str, cl);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}