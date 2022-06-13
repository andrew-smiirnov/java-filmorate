package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

@Component
public interface FilmStorage {

    List<Film> getAll();

    Film create(Film film) throws ValidationException;

    Film put(Film film) throws ValidationException;

    Optional<Film> get(Long filmId) throws ValidationException;

    void delete(Long filmId) throws FilmNotFoundException;
}
