package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Map<String, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if (users.containsKey(user.getEmail())) {
            throw new ValidationException("Пользователь с электронной почтой " +
                    user.getEmail() + " уже зарегистрирован.");
        }
        log.debug("Добавлен новый пользователь: {}", user);
        setNameUserIfNull(user);
        users.put(user.getEmail(), user);
        return user;
    }


    @PutMapping
    public User put(@Valid @RequestBody User user) {
        if (users.containsKey(user.getEmail())) {
            setNameUserIfNull(user);
            log.debug("Обновлена информация о пользователе: {}", user);
            users.replace(user.getEmail(), user);
        } else {
            setNameUserIfNull(user);
            log.debug("Добавлен новый пользователь: {}", user);
            users.put(user.getEmail(), user);
        }
        return user;
    }

    private void setNameUserIfNull(User user) {
        if (user.getName().isBlank() || user.getName() == null) {
            user.setName(user.getLogin());
        }
    }
}
