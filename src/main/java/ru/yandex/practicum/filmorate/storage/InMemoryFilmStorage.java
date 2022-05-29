package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectFilmRealiseDateException;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@Profile("inMemoryStorage")
public class InMemoryFilmStorage implements FilmStorage{
    private Long id = 1L;
    private final Map<Long, Film> films = new ConcurrentHashMap<>();

    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    public Film create(@Valid Film film) throws ValidationException {
        if (films.containsKey(film.getName())) {
            throw new ValidationException("Фильм с таким названием уже находится в базе.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new IncorrectFilmRealiseDateException("Дата релиза должна быть не раньше 28 декабря 1895 года");
        }
        film.setId(id);
        id++;
        films.put(film.getId(), film);
        log.debug("Добавлен новый фильм: {}", film);
        return film;
    }

    public Film put(@Valid Film film) throws ValidationException {
        if (film.getId() < 0 || !films.containsKey(film.getId())) {
            throw new FilmNotFoundException("Film id=" + film.getId() + " not found");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new IncorrectFilmRealiseDateException("Дата релиза должна быть не раньше 28 декабря 1895 года");
        }
        if (films.containsKey(film.getId())) {
            log.debug("Обновлена информация о фильме: {}", film);
            films.replace(film.getId(), film);
        }
        return film;
    }

    @Override
    public Optional<Film> get(Long filmId) throws ValidationException {
        if (filmId < 0 ) {
            throw new IncorrectParameterException("Film id < 0");
        }
        return Optional.ofNullable(films.values().stream()
                .filter(films -> films.getId().equals(filmId))
                .findFirst()
                .orElseThrow(() -> new FilmNotFoundException(String.format("Film id=%d not found", filmId))));
    }

    @Override
    public void delete(Long filmId) throws FilmNotFoundException {
        try {
            films.remove(filmId);
        } catch (RuntimeException e) {
            throw new FilmNotFoundException(String.format("Film id=%d not found", filmId));
        }
    }
}
