package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Component
//@Profile("inMemoryStorage")
public class InMemoryUserStorage implements UserStorage{
    private Long id = 1L;
    private final Map<Long, User> users = new ConcurrentHashMap<>();

    public Collection<User> getAll() {
        return users.values();
    }

    public User create(@Valid User user) throws ValidationException {
        setNameUserIfNull(user);
        user.setId(id);
        id++;
        users.put(user.getId(), user);
        log.debug("Добавлен новый пользователь: {}", user);
        return user;
    }

    public User put(@Valid User user) throws ValidationException {
        if (user.getId() < 0 ) {
            throw new IncorrectParameterException("User id < 0");
        }
        if(!users.containsKey(user.getId())){
            throw new UserNotFoundException(String.format("User с id=%s не найден.", user.getId()));
        }
        setNameUserIfNull(user);
        users.replace(user.getId(), user);
        log.debug("Обновлена информация о пользователе: {}", user);
        return user;
    }

    @Override
    public Optional<User> get(Long userId) throws UserNotFoundException {
        if(!users.containsKey(userId)){
            throw new UserNotFoundException(String.format("User с id=%s не найден.", userId));
        }
        return Optional.ofNullable(users.values().stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(String.format("User с id=%s не найден.", userId))));
    }

    @Override
    public void delete(Long userId) throws ValidationException {
        if (userId < 0 ) {
            throw new IncorrectParameterException("User id < 0");
        }
        if(!users.containsKey(userId)){
            throw new UserNotFoundException(String.format("User с id=%s не найден.", userId));
        }
        users.remove(userId);
    }

    private void setNameUserIfNull(User user) {
        if (StringUtils.isBlank(user.getName())){
            user.setName(user.getLogin());
        }
    }
}
