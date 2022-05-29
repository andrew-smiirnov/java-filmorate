package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FilmControllerTest {

    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mockMvc;

    @Test
    void getAll() {
    }

    @Test
    void create() {
    }

    @Test
    void put() {
    }

    @Test
    void createValidFilmResponseShouldBeOk_thenStatus200() throws Exception {
        Film film = getFilm();
        String body = mapper.writeValueAsString(film);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/films").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[]"));
        this.mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.duration").value("PT1H30M"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.releaseDate").value("2020-10-10"));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/films").content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[" + body + "]"));
    }

    @Test
    void createNotValidNameFilmResponseShouldBeNotOk() throws Exception {
        Film film = getFilm();
        film.setName("");
        String body = mapper.writeValueAsString(film);
        this.mockMvc.perform(post("/films").content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(ValidationException.class));
    }

    @Test
    void createNotValidDescriptionFilmResponseShouldBeNotOk() throws Exception {
        Film film = getFilm();
        String body = mapper.writeValueAsString(film);
        this.mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("description"));
        String abc = "abcdefjhijklmnopqrstuvwxyz";
        film.setDescription(abc + abc + abc + abc + abc + abc + abc + abc);
        body = mapper.writeValueAsString(film);
        this.mockMvc.perform(MockMvcRequestBuilders.put("/films").content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(ValidationException.class));
    }

    @Test
    void createNotValidFilmReleaseDataResponseShouldBeNotOk() throws Exception {
        Film film = getFilm();
        String body = mapper.writeValueAsString(film);
        this.mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.releaseDate").value("2020-10-10"));
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        body = mapper.writeValueAsString(film);
        this.mockMvc.perform(MockMvcRequestBuilders.put("/films").content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.releaseDate").value("1895-12-28"));
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        body = mapper.writeValueAsString(film);
        this.mockMvc.perform(MockMvcRequestBuilders.put("/films").content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(ValidationException.class));
    }

    @Test
    void createNotValidFilmDurationResponseShouldBeNotOk() throws Exception {
        Film film = getFilm();
        String body = mapper.writeValueAsString(film);
        this.mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.duration").value("PT1H30M"));
        film.setDuration(0);
        body = mapper.writeValueAsString(film);
        this.mockMvc.perform(MockMvcRequestBuilders.put("/films").content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.duration").value("PT0S"));
        film.setDuration(-1);
        body = mapper.writeValueAsString(film);
        this.mockMvc.perform(MockMvcRequestBuilders.put("/films").content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(ValidationException.class));
    }

    private Film getFilm() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("description");
        film.setId(Long.valueOf(1));
        film.setReleaseDate(LocalDate.of(2020, 10, 10));
        film.setDuration(90);
        //film.setDuration(Duration.of(90, ChronoUnit.MINUTES));
        return film;
    }
}