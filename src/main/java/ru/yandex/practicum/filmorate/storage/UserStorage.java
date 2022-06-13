package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

@Component
public interface UserStorage {

    Collection<User> getAll();

    User create(User user) throws ValidationException;

    User put(User user) throws ValidationException;

    Optional<User> get(Long userId) throws UserNotFoundException;

    void delete(Long userId) throws ValidationException;
}
