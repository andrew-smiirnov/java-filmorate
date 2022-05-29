package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/films")
public class FilmController {

    @Autowired
    FilmService filmService;

    @Autowired
    FilmStorage filmStorage;


    @GetMapping
    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    @GetMapping("{id}")
    public Film get(@PathVariable Long id) throws ValidationException {
        return filmStorage.get(id).get();
    }

    @GetMapping("/popular")
    public Set<Film> getPopularFilms(@RequestParam(defaultValue = "10") String count) throws FilmNotFoundException {
        return filmService.getPopularFilms(count);
    }

    @PostMapping
    public Film create(@RequestBody @Valid Film film) throws ValidationException {
        return filmStorage.create(film);
    }

    @PutMapping
    public Film put(@RequestBody @Valid Film film) throws ValidationException {
        return filmStorage.put(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable Long id, @PathVariable Long userId) throws ValidationException {
        filmService.likeFilm(id, userId);
        if (id < 0 || userId < 0){
            throw new IncorrectParameterException("Переданный id < 0");
        }
    }

    @DeleteMapping("{id}")
    public void deleteFilm(@PathVariable Long id) throws ValidationException {
        filmStorage.delete(id);
        if (id < 0 ){
            throw new IncorrectParameterException("Переданный id < 0");
        }
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeFilm(@PathVariable Long id, @PathVariable Long userId) throws ValidationException {
        filmService.deleteLikeFilm(id, userId);
        if (id < 0 || userId < 0){
            throw new IncorrectParameterException("Переданный id < 0");
        }
    }
}
