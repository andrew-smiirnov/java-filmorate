package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.StringUtils;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<String, User> users = new ConcurrentHashMap<>();

    @GetMapping
    public Collection<User> getAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) throws ValidationException {
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
        if (StringUtils.isBlank(user.getName())){
            user.setName(user.getLogin());
        }
    }
}
