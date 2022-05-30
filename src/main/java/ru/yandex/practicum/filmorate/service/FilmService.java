package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class FilmService {


    @Autowired
    FilmStorage filmStorage;

    @Autowired
    UserStorage userStorage;



    public void likeFilm(Long id, Long userId) throws ValidationException {
        if(userId < 0 || userStorage.get(userId).isEmpty()){
            throw new UserNotFoundException("User не найден.");
        }
        if(filmStorage.get(id).isEmpty()) {
            throw new FilmNotFoundException(String.format("Film id=%s не найден.", id));
        }
        if(filmStorage.get(id).isPresent()){
            filmStorage.get(id).get().getLikes().add(userId);
        }
    }

    public void deleteLikeFilm(Long id, Long userId) throws ValidationException {
        if(userId < 0 || userStorage.get(userId).isEmpty()){
            throw new UserNotFoundException("User не найден.");
        }
        if(filmStorage.get(id).isEmpty()) {
            throw new FilmNotFoundException(String.format("Film id=%s не найден.", id));
        }
        filmStorage.get(id).get().getLikes().remove(userId);
    }

    public Set<Film> getPopularFilms(String count) {
        return filmStorage.getAll().stream()
                .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                .limit(Integer.parseInt(count))
                .collect(Collectors.toSet());
    }

    public List<Film> getAll(){
        return filmStorage.getAll();
    }

    public Optional<Film> get(Long id) throws ValidationException {
        return filmStorage.get(id);
    }

    public Film create(Film film) throws ValidationException {
        return filmStorage.create(film);
    }

    public Film put(Film film) throws ValidationException {
        return  filmStorage.put(film);
    }

    public void delete(Long id) throws FilmNotFoundException {
        filmStorage.delete(id);
    }
}
